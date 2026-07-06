package com.lira.application.progressplan

import com.lira.domain.progressplan.ProgressPlan
import com.lira.domain.progressplan.ProgressPlanRepository
import com.lira.infrastructure.progressplan.dto.ProgressPlanResponse
import com.lira.infrastructure.progressplan.dto.toResponse
import jakarta.transaction.Transactional
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
@Transactional
class CompleteSubobjective(private val progressPlanRepository: ProgressPlanRepository) {

    private val log = LoggerFactory.getLogger(javaClass)

    fun execute(subobjectiveId: Int, objectiveId: Int, completed: Boolean): ProgressPlanResponse {
        progressPlanRepository.completeSubobjective(subobjectiveId, objectiveId, completed)

        val plan = progressPlanRepository.getProgressPlanBySubobjectiveId(subobjectiveId)
        val allSubs = plan.objectives.flatMap { it.subobjectives }
        plan.totalProgress = if (allSubs.isEmpty()) 0.0 else allSubs.map { it.currentProgress }.average()
        progressPlanRepository.save(plan)

        log.info("Subobjective id=$subobjectiveId marked completed=$completed; plan totalProgress=${"%.0f".format(plan.totalProgress * 100)}%")
        return progressPlanRepository.getProgressPlanBySubobjectiveId(subobjectiveId).toResponse()
    }
}
