package com.lira.application.appointment

import com.lira.domain.appointment.Appointment
import com.lira.domain.appointment.AppointmentRepository
import com.lira.infrastructure.appointment.dto.AppointmentRequest
import com.lira.infrastructure.appointment.dto.toDomain
import org.springframework.stereotype.Service

@Service
class CreateAppointment(
    private val appointmentRepository: AppointmentRepository,
) {
    fun execute(request: AppointmentRequest): Appointment {

        return appointmentRepository.save(request.toDomain())
    }
}

