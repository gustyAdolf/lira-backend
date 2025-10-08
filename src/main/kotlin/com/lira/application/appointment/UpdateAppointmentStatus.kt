package com.lira.application.appointment

import com.lira.domain.appointment.Appointment
import com.lira.domain.appointment.AppointmentRepository
import com.lira.infrastructure.appointment.dto.UpdateAppointmentStatusRequest
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