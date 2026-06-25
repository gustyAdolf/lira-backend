package com.lira.application.progressplan

import com.lira.domain.progressplan.ProgressPlanRepository
import com.lira.infrastructure.progressplan.dto.ProgressPlanResponse
import com.lira.infrastructure.progressplan.dto.toResponse
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class GetProgressPlansForPatient(
    private val progressPlanRepository: ProgressPlanRepository,
) {
    fun execute(patientId: Int): List<ProgressPlanResponse> {
        return progressPlanRepository.getProgressPlansByPatientId(patientId).map { it.toResponse() }
    }
}
