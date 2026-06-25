package com.lira.application.appointment

import com.lira.domain.appointment.AppointmentRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class DeleteAppointment(
    private val appointmentRepository: AppointmentRepository
) {
    private val log = LoggerFactory.getLogger(javaClass)

    fun execute(appointmentId: Int) {
        appointmentRepository.deleteById(appointmentId)
        log.info("Appointment deleted: id=$appointmentId")
    }
}