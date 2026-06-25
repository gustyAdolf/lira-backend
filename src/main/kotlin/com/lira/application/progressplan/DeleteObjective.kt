package com.lira.application.progressplan

import com.lira.domain.progressplan.ProgressPlanRepository
import com.lira.infrastructure.progressplan.dto.ProgressPlanResponse
import com.lira.infrastructure.progressplan.dto.toResponse
import jakarta.transaction.Transactional
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
@Transactional
class DeleteObjective(private val progressPlanRepository: ProgressPlanRepository) {

    private val log = LoggerFactory.getLogger(javaClass)

    fun execute(objectiveId: Int): ProgressPlanResponse {
        val plan = progressPlanRepository.getProgressPlanByObjectiveId(objectiveId)
        val subIds = progressPlanRepository.getObjectiveById(objectiveId).subobjectives.map { it.id }
        subIds.forEach { progressPlanRepository.deleteEntriesBySubobjectiveId(it) }
        progressPlanRepository.deleteObjectiveById(objectiveId)

        val remainingObjectives = plan.objectives.filter { it.id != objectiveId }
        val remaining = remainingObjectives.flatMap { it.subobjectives }
        val newProgress = if (remaining.isEmpty()) 0.0 else remaining.map { it.currentProgress }.average()
        progressPlanRepository.updateTotalProgress(plan.id, newProgress)

        log.info("Objective $objectiveId deleted from plan ${plan.id} (${subIds.size} subobjectives removed)")
        return plan.copy(objectives = remainingObjectives, totalProgress = newProgress).toResponse()
    }
}
