package com.phobos.application.appointment

import com.phobos.domain.appointment.Appointment
import com.phobos.domain.appointment.AppointmentRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class GetNextPatientAppointments(
    private val appointmentRepository: AppointmentRepository
) {
    fun execute(patientId: Int, size: Int): List<Appointment> {
        return appointmentRepository.findNextAppointmentsForPatient(patientId, size)
    }
}