package com.lira.application.progressplan

import com.lira.domain.progressplan.ProgressPlanRepository
import com.lira.domain.user.PatientDisorderRepository
import com.lira.infrastructure.progressplan.dto.ProgressPlanRequest
import com.lira.infrastructure.progressplan.dto.toDomain
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class CreateProgressPlan(
    private val progressPlanRepository: ProgressPlanRepository,
    private val patientDisorderRepository: PatientDisorderRepository
) {
    private val log = LoggerFactory.getLogger(javaClass)

    fun execute(progressPlanRequest: ProgressPlanRequest) {
        progressPlanRepository.save(progressPlanRequest.toDomain())
        if (progressPlanRequest.mentalDisorderId != null) {
            patientDisorderRepository.assignIfAbsent(
                progressPlanRequest.patientId,
                progressPlanRequest.mentalDisorderId
            )
        }
        log.info(
            "Progress plan created: patientId=${progressPlanRequest.patientId}, " +
            "therapistId=${progressPlanRequest.therapistId}, title='${progressPlanRequest.title}', " +
            "mentalDisorderId=${progressPlanRequest.mentalDisorderId}"
        )
    }
}
