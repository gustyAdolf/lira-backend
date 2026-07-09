package com.lira.application.appointment

import com.lira.application.notifications.CreateNotification
import com.lira.domain.appointment.AppointmentRepository
import com.lira.domain.notifications.NotificationRepository
import com.lira.domain.notifications.NotificationType
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Duration
import java.time.LocalDateTime

@Service
@Transactional
class ProcessAppointmentReconfirmations(
    private val appointmentRepository: AppointmentRepository,
    private val notificationRepository: NotificationRepository,
    private val createNotification: CreateNotification,
) {
    private val log = LoggerFactory.getLogger(javaClass)

    fun execute() {
        val now = LocalDateTime.now()
        val candidates = appointmentRepository.findUpcomingActiveAppointments(now, now.plusHours(REMINDER_HOURS_BEFORE))

        candidates.forEach { appointment ->
            if (appointment.patientConfirmedAt != null) return@forEach

            val hoursUntilAppointment = Duration.between(now, appointment.appointmentDate).toHours()

            if (hoursUntilAppointment <= REMINDER_HOURS_BEFORE &&
                !notificationRepository.existsForAppointmentAndType(appointment.id, NotificationType.RECONFIRMATION_REMINDER)
            ) {
                createNotification.execute(
                    recipientUserId = appointment.patient.id,
                    type = NotificationType.RECONFIRMATION_REMINDER,
                    title = "Confirma tu próxima cita",
                    body = "Tu cita del ${appointment.appointmentDate} se acerca. Confirma tu asistencia.",
                    relatedAppointmentId = appointment.id,
                )
                log.info("Reconfirmation reminder sent for appointment ${appointment.id}")
            }

            if (hoursUntilAppointment <= CONFIRMATION_DEADLINE_HOURS &&
                !notificationRepository.existsForAppointmentAndType(appointment.id, NotificationType.RECONFIRMATION_MISSED)
            ) {
                createNotification.execute(
                    recipientUserId = appointment.therapist.id,
                    type = NotificationType.RECONFIRMATION_MISSED,
                    title = "Paciente sin confirmar",
                    body = "El paciente ${appointment.patient.name} no ha confirmado su cita del ${appointment.appointmentDate}.",
                    relatedAppointmentId = appointment.id,
                )
                log.info("Missed-confirmation alert sent for appointment ${appointment.id}")
            }
        }
    }

    companion object {
        const val REMINDER_HOURS_BEFORE = 48L
        const val CONFIRMATION_DEADLINE_HOURS = 24L
    }
}
