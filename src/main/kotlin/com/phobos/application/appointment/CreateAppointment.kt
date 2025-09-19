package com.phobos.application.appointment

import com.phobos.domain.appointment.Appointment
import com.phobos.domain.appointment.AppointmentRepository
import com.phobos.infrastructure.appointment.dto.AppointmentRequest
import com.phobos.infrastructure.appointment.dto.toDomain
import org.springframework.stereotype.Service

@Service
class CreateAppointment(
    private val appointmentRepository: AppointmentRepository,
) {
    fun execute(request: AppointmentRequest): Appointment {

        return appointmentRepository.save(request.toDomain())
    }
}

