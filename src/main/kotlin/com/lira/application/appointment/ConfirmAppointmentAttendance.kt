package com.lira.application.appointment

import com.lira.domain.appointment.Appointment
import com.lira.domain.appointment.AppointmentRepository
import com.lira.domain.appointment.AppointmentStatus
import com.lira.domain.exceptions.AppointmentException
import org.slf4j.LoggerFactory
import org.springframework.security.access.AccessDeniedException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class ConfirmAppointmentAttendance(
    private val appointmentRepository: AppointmentRepository
) {
    private val log = LoggerFactory.getLogger(javaClass)

    fun execute(appointmentId: Int, requestingPatientId: Int): Appointment {
        val appointment = appointmentRepository.findById(appointmentId)

        if (appointment.patient.id != requestingPatientId) {
            throw AccessDeniedException("No puedes confirmar la asistencia de la cita de otro paciente")
        }
        if (appointment.status == AppointmentStatus.CANCELLED) {
            throw AppointmentException.NotEditable()
        }

        val confirmed = appointmentRepository.confirmAttendance(appointmentId)
        log.info("Appointment $appointmentId confirmed by patient $requestingPatientId")
        return confirmed
    }
}
