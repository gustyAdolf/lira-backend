package com.lira.application.appointment

import com.lira.domain.appointment.AppointmentRepository
import org.springframework.stereotype.Service

@Service
class DeleteAppointment(
    private val appointmentRepository: AppointmentRepository
) {
    fun execute(appointmentId: Int) {
        appointmentRepository.deleteById(appointmentId)
    }
}