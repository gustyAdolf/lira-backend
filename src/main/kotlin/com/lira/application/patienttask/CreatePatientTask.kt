package com.lira.application.patienttask

import com.lira.domain.patienttask.PatientTask
import com.lira.domain.patienttask.PatientTaskRepository
import com.lira.infrastructure.patienttask.dto.PatientTaskRequest
import com.lira.infrastructure.patienttask.dto.toDomain
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class CreatePatientTask(
    private val patientTaskRepository: PatientTaskRepository,
) {
    fun execute(request: PatientTaskRequest): PatientTask =
        patientTaskRepository.save(request.toDomain())
}
