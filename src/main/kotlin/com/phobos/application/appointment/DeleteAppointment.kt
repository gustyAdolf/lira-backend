package com.phobos.application.appointment

import com.phobos.domain.appointment.AppointmentRepository
import org.springframework.stereotype.Service

@Service
class DeleteAppointment(
    private val appointmentRepository: AppointmentRepository
) {
    fun execute(appointmentId: Int) {
        appointmentRepository.deleteById(appointmentId)
    }
}