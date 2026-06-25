package com.lira.application.progressplan

import com.lira.domain.progressplan.ProgressPlanRepository
import com.lira.infrastructure.progressplan.dto.ProgressPlanResponse
import com.lira.infrastructure.progressplan.dto.toResponse
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
@Transactional
class UpdateObjective(private val progressPlanRepository: ProgressPlanRepository) {

    fun execute(objectiveId: Int, title: String, description: String?): ProgressPlanResponse {
        val objective = progressPlanRepository.getObjectiveById(objectiveId)
        val updated = objective.copy(title = title, description = description)
        progressPlanRepository.updateObjective(updated)
        return progressPlanRepository.getProgressPlanByObjectiveId(objectiveId).toResponse()
    }
}
