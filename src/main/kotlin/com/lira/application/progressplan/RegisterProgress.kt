package com.lira.application.progressplan

import com.lira.domain.progressplan.ProgressPlan
import com.lira.domain.progressplan.ProgressPlanRepository
import com.lira.infrastructure.progressplan.dto.SubobjectiveEntryRequest
import com.lira.infrastructure.progressplan.dto.toDomain
import com.lira.domain.progressplan.SubobjectiveType
import jakarta.transaction.Transactional
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
@Transactional
class RegisterProgress(
    private val progressPlanRepository: ProgressPlanRepository,
) {
    private val log = LoggerFactory.getLogger(javaClass)

    fun execute(entryRequest: SubobjectiveEntryRequest): ProgressPlan {
        progressPlanRepository.saveSubobjectiveEntry(entryRequest.toDomain())

        val subobjective = progressPlanRepository.getSubobjectiveById(entryRequest.subobjectiveId)

        if (SubobjectiveType.QUANTITATIVE == subobjective.type) {
            val increment = entryRequest.valueIncrement ?: 0
            subobjective.currentValue = maxOf(0, subobjective.currentValue + increment)
            subobjective.currentProgress = (subobjective.currentValue.toDouble() / (subobjective.targetValue ?: 1)).coerceAtMost(1.0)
        } else {
            // QUALITATIVE: update counters for clinical record only; progress is controlled by isCompleted
            if (entryRequest.isSuccess == true) subobjective.currentSuccess++ else subobjective.currentFail++
        }
        progressPlanRepository.saveSubobjective(subobjective, entryRequest.objectiveId)
        log.info(
            "Progress registered: subobjectiveId=${entryRequest.subobjectiveId}, " +
            "progress=${"%.0f".format(subobjective.currentProgress * 100)}%"
        )

        val plan = progressPlanRepository.getProgressPlanBySubobjectiveId(entryRequest.subobjectiveId)
        val allSubobjectives = plan.objectives.flatMap { it.subobjectives }
        plan.totalProgress = allSubobjectives.map { it.currentProgress }.average()
        progressPlanRepository.save(plan)
        return plan
    }
}