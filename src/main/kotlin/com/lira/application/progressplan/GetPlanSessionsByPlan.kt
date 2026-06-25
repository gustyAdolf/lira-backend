package com.lira.application.progressplan

import com.lira.domain.progressplan.PlanSessionRepository
import com.lira.domain.progressplan.ProgressPlanRepository
import com.lira.infrastructure.progressplan.dto.PlanSessionResponse
import com.lira.infrastructure.progressplan.dto.toResponse
import org.springframework.stereotype.Service

@Service
class GetPlanSessionsByPlan(
    private val planSessionRepository: PlanSessionRepository,
    private val progressPlanRepository: ProgressPlanRepository
) {
    fun execute(planId: Int): List<PlanSessionResponse> {
        return planSessionRepository.findByPlanId(planId).map { session ->
            val entries = progressPlanRepository.findEntriesByPlanSessionId(session.id)
            session.toResponse(entries)
        }
    }
}
