package com.phobos.application.appointment

import com.phobos.domain.appointment.Appointment
import com.phobos.domain.appointment.AppointmentRepository
import com.phobos.infrastructure.appointment.dto.UpdateAppointmentStatusRequest
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
@Transactional
class UpdateAppointmentStatus(
    private val appointmentRepository: AppointmentRepository
) {
    fun execute(
        appointmentId: Int,
        appointmentStatus: UpdateAppointmentStatusRequest
    ): Appointment {
        return appointmentRepository.updateAppointmentStatus(appointmentId, appointmentStatus.status)
    }
}