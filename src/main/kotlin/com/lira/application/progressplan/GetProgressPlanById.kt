package com.lira.application.progressplan

import com.lira.domain.progressplan.ProgressPlanRepository
import com.lira.infrastructure.progressplan.dto.ProgressPlanResponse
import com.lira.infrastructure.progressplan.dto.toResponse
import org.springframework.stereotype.Service

@Service
class GetProgressPlanById(private val progressPlanRepository: ProgressPlanRepository) {
    fun execute(planId: Int): ProgressPlanResponse =
        progressPlanRepository.getProgressPlanById(planId).toResponse()
}
