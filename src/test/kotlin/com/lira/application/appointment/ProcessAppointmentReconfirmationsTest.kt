package com.lira.application.appointment

import com.lira.application.notifications.CreateNotification
import com.lira.domain.appointment.Appointment
import com.lira.domain.appointment.AppointmentRepository
import com.lira.domain.appointment.AppointmentStatus
import com.lira.domain.appointment.AppointmentType
import com.lira.domain.notifications.NotificationRepository
import com.lira.domain.notifications.NotificationType
import com.lira.domain.user.Patient
import com.lira.domain.user.Therapist
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.time.LocalDateTime

class ProcessAppointmentReconfirmationsTest {

    private lateinit var appointmentRepository: AppointmentRepository
    private lateinit var notificationRepository: NotificationRepository
    private lateinit var createNotification: CreateNotification
    private lateinit var processAppointmentReconfirmations: ProcessAppointmentReconfirmations

    private fun appointment(appointmentDate: LocalDateTime, patientConfirmedAt: LocalDateTime? = null) = Appointment(
        id = 1,
        therapist = Therapist(id = 6),
        patient = Patient(id = 3),
        appointmentType = AppointmentType.GENERAL,
        appointmentDate = appointmentDate,
        appointmentDuration = 60,
        description = null,
        cost = BigDecimal("25.00"),
        status = AppointmentStatus.CONFIRMED,
        patientConfirmedAt = patientConfirmedAt,
    )

    @BeforeEach
    fun setUp() {
        appointmentRepository = mockk()
        notificationRepository = mockk()
        createNotification = mockk(relaxed = true)
        processAppointmentReconfirmations =
            ProcessAppointmentReconfirmations(appointmentRepository, notificationRepository, createNotification)
    }

    @Test
    fun `execute crea recordatorio al paciente cuando faltan 48h y no hay recordatorio previo`() {
        // given
        val appt = appointment(LocalDateTime.now().plusHours(48))
        every { appointmentRepository.findUpcomingActiveAppointments(any(), any()) } returns listOf(appt)
        every {
            notificationRepository.existsForAppointmentAndType(1, NotificationType.RECONFIRMATION_REMINDER)
        } returns false
        every {
            notificationRepository.existsForAppointmentAndType(1, NotificationType.RECONFIRMATION_MISSED)
        } returns true

        // when
        processAppointmentReconfirmations.execute()

        // then
        verify(exactly = 1) {
            createNotification.execute(
                recipientUserId = 3,
                type = NotificationType.RECONFIRMATION_REMINDER,
                title = any(),
                body = any(),
                relatedAppointmentId = 1,
            )
        }
    }

    @Test
    fun `execute no duplica el recordatorio si ya fue enviado`() {
        // given
        val appt = appointment(LocalDateTime.now().plusHours(40))
        every { appointmentRepository.findUpcomingActiveAppointments(any(), any()) } returns listOf(appt)
        every {
            notificationRepository.existsForAppointmentAndType(1, NotificationType.RECONFIRMATION_REMINDER)
        } returns true
        every {
            notificationRepository.existsForAppointmentAndType(1, NotificationType.RECONFIRMATION_MISSED)
        } returns true

        // when
        processAppointmentReconfirmations.execute()

        // then
        verify(exactly = 0) {
            createNotification.execute(
                recipientUserId = any(),
                type = NotificationType.RECONFIRMATION_REMINDER,
                title = any(),
                body = any(),
                relatedAppointmentId = any(),
            )
        }
    }

    @Test
    fun `execute no crea recordatorio si el paciente ya confirmo`() {
        // given
        val appt = appointment(LocalDateTime.now().plusHours(40), patientConfirmedAt = LocalDateTime.now())
        every { appointmentRepository.findUpcomingActiveAppointments(any(), any()) } returns listOf(appt)

        // when
        processAppointmentReconfirmations.execute()

        // then
        verify(exactly = 0) { createNotification.execute(any(), any(), any(), any(), any()) }
        verify(exactly = 0) { notificationRepository.existsForAppointmentAndType(any(), any()) }
    }

    @Test
    fun `execute crea aviso al terapeuta cuando faltan 24h y el paciente no confirmo`() {
        // given
        val appt = appointment(LocalDateTime.now().plusHours(20))
        every { appointmentRepository.findUpcomingActiveAppointments(any(), any()) } returns listOf(appt)
        every {
            notificationRepository.existsForAppointmentAndType(1, NotificationType.RECONFIRMATION_REMINDER)
        } returns true
        every {
            notificationRepository.existsForAppointmentAndType(1, NotificationType.RECONFIRMATION_MISSED)
        } returns false

        // when
        processAppointmentReconfirmations.execute()

        // then
        verify(exactly = 1) {
            createNotification.execute(
                recipientUserId = 6,
                type = NotificationType.RECONFIRMATION_MISSED,
                title = any(),
                body = any(),
                relatedAppointmentId = 1,
            )
        }
    }

    @Test
    fun `execute no crea aviso de no confirmacion si el paciente ya confirmo`() {
        // given
        val appt = appointment(LocalDateTime.now().plusHours(20), patientConfirmedAt = LocalDateTime.now())
        every { appointmentRepository.findUpcomingActiveAppointments(any(), any()) } returns listOf(appt)

        // when
        processAppointmentReconfirmations.execute()

        // then
        verify(exactly = 0) {
            createNotification.execute(
                recipientUserId = any(),
                type = NotificationType.RECONFIRMATION_MISSED,
                title = any(),
                body = any(),
                relatedAppointmentId = any(),
            )
        }
    }
}
