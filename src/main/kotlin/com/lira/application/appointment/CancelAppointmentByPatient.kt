package com.lira.application.appointment

import com.lira.application.notifications.CreateNotification
import com.lira.domain.appointment.Appointment
import com.lira.domain.appointment.AppointmentRepository
import com.lira.domain.appointment.AppointmentStatus
import com.lira.domain.exceptions.AppointmentException
import com.lira.domain.notifications.NotificationType
import com.lira.domain.user.Company
import com.lira.domain.user.UserRepository
import org.slf4j.LoggerFactory
import org.springframework.security.access.AccessDeniedException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Duration
import java.time.LocalDateTime

@Service
@Transactional
class CancelAppointmentByPatient(
    private val appointmentRepository: AppointmentRepository,
    private val userRepository: UserRepository,
    private val createNotification: CreateNotification,
) {
    private val log = LoggerFactory.getLogger(javaClass)

    fun execute(appointmentId: Int, requestingPatientId: Int): Appointment {
        val appointment = appointmentRepository.findById(appointmentId)

        if (appointment.patient.id != requestingPatientId) {
            throw AccessDeniedException("No puedes cancelar la cita de otro paciente")
        }

        val windowHours = resolveCancellationWindowHours(appointment.companyId)
        val hoursUntilAppointment = Duration.between(LocalDateTime.now(), appointment.appointmentDate).toHours()

        if (hoursUntilAppointment < windowHours) {
            throw AppointmentException.CancellationWindowExpired()
        }

        val cancelled = appointmentRepository.updateAppointmentStatus(appointmentId, AppointmentStatus.CANCELLED)
        createNotification.execute(
            recipientUserId = appointment.therapist.id,
            type = NotificationType.APPOINTMENT_CANCELLED,
            title = "Cita cancelada",
            body = "El paciente ${appointment.patient.name} canceló su cita del ${appointment.appointmentDate}",
            relatedAppointmentId = appointment.id,
        )
        log.info("Appointment $appointmentId cancelled directly by patient $requestingPatientId")
        return cancelled
    }

    private fun resolveCancellationWindowHours(companyId: Int?): Int {
        val company = companyId?.let { userRepository.findById(it) as? Company }
        return company?.cancellationWindowHours ?: DEFAULT_CANCELLATION_WINDOW_HOURS
    }

    companion object {
        private const val DEFAULT_CANCELLATION_WINDOW_HOURS = 24
    }
}
