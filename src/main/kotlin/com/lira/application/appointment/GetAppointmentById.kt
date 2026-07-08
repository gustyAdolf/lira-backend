package com.lira.application.appointment

import com.lira.domain.appointment.Appointment
import com.lira.domain.appointment.AppointmentRepository
import org.springframework.stereotype.Service

@Service
class GetAppointmentById(
    private val appointmentRepository: AppointmentRepository
) {
    fun execute(appointmentId: Int): Appointment = appointmentRepository.findById(appointmentId)
}
