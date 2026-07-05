package com.lira.application.patienttask

import com.lira.domain.patienttask.PatientTaskRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class DeletePatientTask(
    private val patientTaskRepository: PatientTaskRepository,
) {
    fun execute(id: Int) = patientTaskRepository.deleteById(id)
}
