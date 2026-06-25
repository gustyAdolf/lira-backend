package com.lira.application.appointment

import com.lira.domain.appointment.Appointment
import com.lira.domain.appointment.AppointmentRepository
import com.lira.domain.appointment.AppointmentStatus
import com.lira.domain.exceptions.AppointmentException
import com.lira.infrastructure.appointment.dto.UpdateAppointmentRequest
import jakarta.transaction.Transactional
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
@Transactional
class UpdateAppointment(
    private val appointmentRepository: AppointmentRepository
) {
    private val log = LoggerFactory.getLogger(javaClass)

    fun execute(appointmentId: Int, request: UpdateAppointmentRequest): Appointment {
        val existing = appointmentRepository.findById(appointmentId)

        if (existing.status != AppointmentStatus.PENDING && existing.status != AppointmentStatus.CONFIRMED) {
            log.warn("Attempt to edit appointment $appointmentId in non-editable status: ${existing.status}")
            throw AppointmentException.NotEditable()
        }

        val updated = existing.copy(
            progressPlanId = request.progressPlanId,
            appointmentType = request.appointmentType,
            therapistNotes = request.therapistNotes,
            appointmentDate = request.appointmentDate,
            appointmentDuration = request.appointmentDuration,
            cost = request.cost,
            description = request.description
        )

        val result = appointmentRepository.update(updated)
        log.info("Appointment $appointmentId updated: date=${request.appointmentDate}")
        return result
    }
}
