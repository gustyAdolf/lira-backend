package com.lira.application.progressplan

import com.lira.domain.progressplan.ProgressPlanRepository
import com.lira.infrastructure.progressplan.dto.ProgressPlanResponse
import com.lira.infrastructure.progressplan.dto.toResponse
import jakarta.transaction.Transactional
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
@Transactional
class DeleteSubobjective(private val progressPlanRepository: ProgressPlanRepository) {

    private val log = LoggerFactory.getLogger(javaClass)

    fun execute(subobjectiveId: Int): ProgressPlanResponse {
        val plan = progressPlanRepository.getProgressPlanBySubobjectiveId(subobjectiveId)
        progressPlanRepository.deleteEntriesBySubobjectiveId(subobjectiveId)
        progressPlanRepository.deleteSubobjectiveById(subobjectiveId)

        val updatedObjectives = plan.objectives.map { obj ->
            obj.copy(subobjectives = obj.subobjectives.filter { it.id != subobjectiveId })
        }
        val remaining = updatedObjectives.flatMap { it.subobjectives }
        val newProgress = if (remaining.isEmpty()) 0.0 else remaining.map { it.currentProgress }.average()
        progressPlanRepository.updateTotalProgress(plan.id, newProgress)

        log.info("Subobjective $subobjectiveId deleted from plan ${plan.id}")
        return plan.copy(objectives = updatedObjectives, totalProgress = newProgress).toResponse()
    }
}
