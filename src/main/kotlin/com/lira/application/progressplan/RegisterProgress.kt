package com.lira.application.progressplan

import com.lira.domain.progressplan.ProgressPlan
import com.lira.domain.progressplan.ProgressPlanRepository
import com.lira.infrastructure.progressplan.dto.SubobjectiveEntryRequest
import com.lira.infrastructure.progressplan.dto.toDomain
import com.lira.domain.progressplan.SubobjectiveType
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
@Transactional
class RegisterProgress(
    private val progressPlanRepository: ProgressPlanRepository,
) {
    fun execute(entryRequest: SubobjectiveEntryRequest): ProgressPlan {
        progressPlanRepository.saveSubobjectiveEntry(entryRequest.toDomain())

        val subobjective = progressPlanRepository.getSubobjectiveById(entryRequest.subobjectiveId)

        if (SubobjectiveType.QUANTITATIVE == subobjective.type) {
            val currentSum = progressPlanRepository.sumValueBySubobjective(entryRequest.subobjectiveId)
            subobjective.currentProgress = (currentSum / (subobjective.targetValue ?: 1)).coerceAtMost(1.0)
        } else {
            val totalSuccesses = progressPlanRepository.countSuccessesBySubobjective(entryRequest.subobjectiveId)
            subobjective.currentProgress = (totalSuccesses / (subobjective.targetSuccess ?: 1)).coerceAtMost(1.0)
        }
        progressPlanRepository.saveSubobjective(subobjective, entryRequest.objectiveId)

        val plan = progressPlanRepository.getProgressPlanBySubobjectiveId(entryRequest.subobjectiveId)
        val allSubobjectives = plan.objectives.flatMap { it.subobjectives }
        plan.totalProgress = allSubobjectives.map { it.currentProgress }.average()
        progressPlanRepository.save(plan)
        return plan
    }
}