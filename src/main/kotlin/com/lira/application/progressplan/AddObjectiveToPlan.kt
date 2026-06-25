package com.lira.application.progressplan

import com.lira.domain.progressplan.Objective
import com.lira.domain.progressplan.ProgressPlanRepository
import com.lira.infrastructure.progressplan.dto.ProgressPlanResponse
import com.lira.infrastructure.progressplan.dto.toResponse
import jakarta.transaction.Transactional
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
@Transactional
class AddObjectiveToPlan(private val progressPlanRepository: ProgressPlanRepository) {

    private val log = LoggerFactory.getLogger(javaClass)

    fun execute(planId: Int, title: String, description: String?): ProgressPlanResponse {
        val existing = progressPlanRepository.getObjectivesByPlanId(planId)
        val objective = Objective(
            title = title,
            description = description,
            orderIndex = existing.size,
            subobjectives = emptyList()
        )
        progressPlanRepository.saveObjective(objective, planId)
        log.info("Objective added to plan $planId: '$title'")
        return progressPlanRepository.getProgressPlanById(planId).toResponse()
    }
}
