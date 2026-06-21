package com.lira.application.progressplan

import com.lira.domain.progressplan.ProgressPlanRepository
import com.lira.infrastructure.progressplan.dto.ProgressPlanRequest
import com.lira.infrastructure.progressplan.dto.toDomain
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class CreateProgressPlan(
    private val progressPlanRepository: ProgressPlanRepository
) {
    fun execute(progressPlanRequest: ProgressPlanRequest) {
        progressPlanRepository.save(progressPlanRequest.toDomain())
    }
}