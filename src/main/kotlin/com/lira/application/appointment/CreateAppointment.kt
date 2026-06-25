package com.lira.application.appointment

import com.lira.domain.appointment.Appointment
import com.lira.domain.appointment.AppointmentRepository
import com.lira.infrastructure.appointment.dto.AppointmentRequest
import com.lira.infrastructure.appointment.dto.toDomain
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class CreateAppointment(
    private val appointmentRepository: AppointmentRepository,
) {
    private val log = LoggerFactory.getLogger(javaClass)

    fun execute(request: AppointmentRequest): Appointment {
        val appointment = appointmentRepository.save(request.toDomain())
        log.info(
            "Appointment created: id=${appointment.id}, type=${request.appointmentType}, " +
            "planId=${request.progressPlanId}, patientId=${request.userId}, therapistId=${request.therapistId}"
        )
        return appointment
    }
}
