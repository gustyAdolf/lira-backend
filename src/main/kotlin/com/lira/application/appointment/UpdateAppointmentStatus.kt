package com.lira.application.appointment

import com.lira.domain.appointment.Appointment
import com.lira.domain.appointment.AppointmentRepository
import com.lira.infrastructure.appointment.dto.UpdateAppointmentStatusRequest
import jakarta.transaction.Transactional
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
@Transactional
class UpdateAppointmentStatus(
    private val appointmentRepository: AppointmentRepository
) {
    private val log = LoggerFactory.getLogger(javaClass)

    fun execute(
        appointmentId: Int,
        appointmentStatus: UpdateAppointmentStatusRequest
    ): Appointment {
        val updated = appointmentRepository.updateAppointmentStatus(appointmentId, appointmentStatus.status)
        log.info("Appointment $appointmentId status updated to ${appointmentStatus.status}")
        return updated
    }
}