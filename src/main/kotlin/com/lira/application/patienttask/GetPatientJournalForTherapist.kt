package com.lira.application.patienttask

import com.lira.domain.appointment.AppointmentRepository
import com.lira.domain.patienttask.PatientTaskEntryRepository
import com.lira.domain.patienttask.PatientTaskJournalEntry
import org.springframework.security.access.AccessDeniedException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class GetPatientJournalForTherapist(
    private val patientTaskEntryRepository: PatientTaskEntryRepository,
    private val appointmentRepository: AppointmentRepository,
) {
    fun execute(patientId: Int, therapistId: Int): List<PatientTaskJournalEntry> {
        if (patientId !in appointmentRepository.findPatientIdsByTherapistId(therapistId)) {
            throw AccessDeniedException("El terapeuta no tiene acceso al diario de este paciente")
        }
        return patientTaskEntryRepository.findJournalForPatient(patientId)
    }
}
