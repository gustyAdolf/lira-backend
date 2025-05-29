package com.phobos.application.appointment

import com.phobos.domain.appointment.Appointment
import com.phobos.domain.appointment.AppointmentRepository
import com.phobos.infrastructure.appointment.AppointmentRequest
import org.springframework.stereotype.Service

@Service
class CreateAppointment(
    private val appointmentRepository: AppointmentRepository,
) {
    fun execute(request: AppointmentRequest): Appointment {
        val appointment = Appointment(
            therapistId = request.therapistId,
            userId = request.userId,
            mentalDisorderId = request.mentalDisorderId,
            appointmentDate = request.appointmentDate,
            appointmentDuration = request.appointmentDuration,
            description = request.description
        )
        return appointmentRepository.save(appointment)
    }
}