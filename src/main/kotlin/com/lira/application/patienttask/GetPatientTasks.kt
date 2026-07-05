package com.lira.application.patienttask

import com.lira.domain.patienttask.PatientTask
import com.lira.domain.patienttask.PatientTaskRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class GetPatientTasks(
    private val patientTaskRepository: PatientTaskRepository,
) {
    fun execute(patientId: Int): List<PatientTask> = patientTaskRepository.findByPatientId(patientId)
}
