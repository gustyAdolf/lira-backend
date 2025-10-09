package com.lira.application.appointment

import com.lira.domain.appointment.Appointment
import com.lira.domain.appointment.AppointmentRepository
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