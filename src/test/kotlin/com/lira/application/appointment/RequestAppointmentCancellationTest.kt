package com.lira.application.appointment

import com.lira.application.notifications.CreateNotification
import com.lira.domain.appointment.Appointment
import com.lira.domain.appointment.AppointmentRepository
import com.lira.domain.appointment.AppointmentStatus
import com.lira.domain.appointment.AppointmentType
import com.lira.domain.notifications.NotificationType
import com.lira.domain.user.Patient
import com.lira.domain.user.Therapist
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.security.access.AccessDeniedException
import java.math.BigDecimal
import java.time.LocalDateTime

class RequestAppointmentCancellationTest {

    private lateinit var appointmentRepository: AppointmentRepository
    private lateinit var createNotification: CreateNotification
    private lateinit var requestAppointmentCancellation: RequestAppointmentCancellation

    private fun appointment() = Appointment(
        id = 1,
        therapist = Therapist(id = 6),
        patient = Patient(id = 3),
        appointmentType = AppointmentType.GENERAL,
        appointmentDate = LocalDateTime.now().plusHours(5),
        appointmentDuration = 60,
        description = null,
        cost = BigDecimal("25.00"),
        status = AppointmentStatus.PENDING,
    )

    @BeforeEach
    fun setUp() {
        appointmentRepository = mockk()
        createNotification = mockk(relaxed = true)
        requestAppointmentCancellation = RequestAppointmentCancellation(appointmentRepository, createNotification)
    }

    @Test
    fun `execute solicita la cancelacion y notifica al terapeuta`() {
        // given
        val existing = appointment()
        val updated = existing.copy(status = AppointmentStatus.CANCELLATION_REQUEST)
        every { appointmentRepository.findById(1) } returns existing
        every {
            appointmentRepository.updateAppointmentStatus(1, AppointmentStatus.CANCELLATION_REQUEST)
        } returns updated

        // when
        val result = requestAppointmentCancellation.execute(1, requestingPatientId = 3)

        // then
        assertEquals(AppointmentStatus.CANCELLATION_REQUEST, result.status)
        verify(exactly = 1) {
            createNotification.execute(
                recipientUserId = 6,
                type = NotificationType.CANCELLATION_REQUESTED,
                title = any(),
                body = any(),
                relatedAppointmentId = 1,
            )
        }
    }

    @Test
    fun `execute rechaza solicitar cancelacion de la cita de otro paciente`() {
        // given
        every { appointmentRepository.findById(1) } returns appointment()

        // when / then
        assertThrows<AccessDeniedException> {
            requestAppointmentCancellation.execute(1, requestingPatientId = 99)
        }
        verify(exactly = 0) { appointmentRepository.updateAppointmentStatus(any(), any()) }
    }
}
