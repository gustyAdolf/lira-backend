package com.lira.application.patienttask

import com.lira.domain.patienttask.PatientTaskRepository
import org.springframework.security.access.AccessDeniedException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class DeletePatientTask(
    private val patientTaskRepository: PatientTaskRepository,
) {
    fun execute(id: Int, requesterId: Int) {
        val task = patientTaskRepository.findById(id) ?: throw NoSuchElementException("task for id=$id does not exist")
        if (task.patientId != requesterId) {
            throw AccessDeniedException("No puedes eliminar una tarea de otro paciente")
        }
        patientTaskRepository.deleteById(id)
    }
}
