package com.lira.application.appointment

import com.lira.application.notifications.CreateNotification
import com.lira.domain.appointment.Appointment
import com.lira.domain.appointment.AppointmentRepository
import com.lira.domain.appointment.AppointmentStatus
import com.lira.domain.notifications.NotificationType
import org.slf4j.LoggerFactory
import org.springframework.security.access.AccessDeniedException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class RequestAppointmentCancellation(
    private val appointmentRepository: AppointmentRepository,
    private val createNotification: CreateNotification,
) {
    private val log = LoggerFactory.getLogger(javaClass)

    fun execute(appointmentId: Int, requestingPatientId: Int): Appointment {
        val appointment = appointmentRepository.findById(appointmentId)

        if (appointment.patient.id != requestingPatientId) {
            throw AccessDeniedException("No puedes solicitar la cancelación de la cita de otro paciente")
        }

        val updated =
            appointmentRepository.updateAppointmentStatus(appointmentId, AppointmentStatus.CANCELLATION_REQUEST)
        createNotification.execute(
            recipientUserId = appointment.therapist.id,
            type = NotificationType.CANCELLATION_REQUESTED,
            title = "Solicitud de cancelación",
            body = "El paciente ${appointment.patient.name} ha solicitado cancelar su cita del ${appointment.appointmentDate}",
            relatedAppointmentId = appointment.id,
        )
        log.info("Appointment $appointmentId cancellation requested by patient $requestingPatientId")
        return updated
    }
}
