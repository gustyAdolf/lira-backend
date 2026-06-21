package com.lira.application.progressplan

import com.lira.domain.progressplan.ProgressPlanRepository
import com.lira.infrastructure.progressplan.dto.ProgressPlanResponse
import com.lira.infrastructure.progressplan.dto.toResponse
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class GetProgressPlanByPatientId(
    private val progressPlanRepository: ProgressPlanRepository,
) {

    fun execute(patientId: Int, therapistId: Int): List<ProgressPlanResponse> {
        val progressPlans =
            progressPlanRepository.getProgressPlanByPatientId(patientId, therapistId).map { it.toResponse() }
        progressPlans.forEach { progressPlan ->
            progressPlan.objectives.forEach { objective ->
                objective.subobjectives.forEach { subobjective ->
                    subobjective.currentValue = progressPlanRepository.sumValueBySubobjective(subobjective.id).toInt()
                    subobjective.currentSuccess = progressPlanRepository.countSuccessesBySubobjective(subobjective.id).toInt()
                }
            }
        }
        return progressPlans
    }
}