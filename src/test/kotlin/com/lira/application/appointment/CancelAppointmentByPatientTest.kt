package com.lira.application.appointment

import com.lira.application.notifications.CreateNotification
import com.lira.domain.appointment.Appointment
import com.lira.domain.appointment.AppointmentRepository
import com.lira.domain.appointment.AppointmentStatus
import com.lira.domain.appointment.AppointmentType
import com.lira.domain.exceptions.AppointmentException
import com.lira.domain.notifications.NotificationType
import com.lira.domain.user.Company
import com.lira.domain.user.Patient
import com.lira.domain.user.Therapist
import com.lira.domain.user.UserRepository
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

class CancelAppointmentByPatientTest {

    private lateinit var appointmentRepository: AppointmentRepository
    private lateinit var userRepository: UserRepository
    private lateinit var createNotification: CreateNotification
    private lateinit var cancelAppointmentByPatient: CancelAppointmentByPatient

    private fun appointment(appointmentDate: LocalDateTime, companyId: Int? = 10) = Appointment(
        id = 1,
        therapist = Therapist(id = 6),
        patient = Patient(id = 3),
        appointmentType = AppointmentType.GENERAL,
        appointmentDate = appointmentDate,
        appointmentDuration = 60,
        description = null,
        cost = BigDecimal("25.00"),
        status = AppointmentStatus.CONFIRMED,
        companyId = companyId,
    )

    private fun company(windowHours: Int) = Company(
        id = 10,
        name = "Clinica",
        email = "clinica@lira.com",
        password = "x",
        profileImagePath = null,
        releaseDate = null,
        cif = null,
        companyAddress = null,
        cancellationWindowHours = windowHours,
    )

    @BeforeEach
    fun setUp() {
        appointmentRepository = mockk()
        userRepository = mockk()
        createNotification = mockk(relaxed = true)
        cancelAppointmentByPatient = CancelAppointmentByPatient(appointmentRepository, userRepository, createNotification)
    }

    @Test
    fun `execute cancela directo y notifica al terapeuta cuando hay suficiente antelacion`() {
        // given
        val existing = appointment(LocalDateTime.now().plusHours(48))
        val cancelled = existing.copy(status = AppointmentStatus.CANCELLED)
        every { appointmentRepository.findById(1) } returns existing
        every { userRepository.findById(10) } returns company(windowHours = 24)
        every { appointmentRepository.updateAppointmentStatus(1, AppointmentStatus.CANCELLED) } returns cancelled

        // when
        val result = cancelAppointmentByPatient.execute(1, requestingPatientId = 3)

        // then
        assertEquals(AppointmentStatus.CANCELLED, result.status)
        verify(exactly = 1) {
            createNotification.execute(
                recipientUserId = 6,
                type = NotificationType.APPOINTMENT_CANCELLED,
                title = any(),
                body = any(),
                relatedAppointmentId = 1,
            )
        }
    }

    @Test
    fun `execute rechaza cancelar dentro de la ventana configurada por la clinica`() {
        // given
        val existing = appointment(LocalDateTime.now().plusHours(10))
        every { appointmentRepository.findById(1) } returns existing
        every { userRepository.findById(10) } returns company(windowHours = 24)

        // when / then
        assertThrows<AppointmentException.CancellationWindowExpired> {
            cancelAppointmentByPatient.execute(1, requestingPatientId = 3)
        }
        verify(exactly = 0) { appointmentRepository.updateAppointmentStatus(any(), any()) }
        verify(exactly = 0) { createNotification.execute(any(), any(), any(), any(), any()) }
    }

    @Test
    fun `execute rechaza cancelar la cita de otro paciente`() {
        // given
        val existing = appointment(LocalDateTime.now().plusHours(48))
        every { appointmentRepository.findById(1) } returns existing

        // when / then
        assertThrows<AccessDeniedException> {
            cancelAppointmentByPatient.execute(1, requestingPatientId = 99)
        }
        verify(exactly = 0) { appointmentRepository.updateAppointmentStatus(any(), any()) }
    }

    @Test
    fun `execute usa una ventana por defecto cuando la cita no tiene clinica asociada`() {
        // given
        val existing = appointment(LocalDateTime.now().plusHours(30), companyId = null)
        val cancelled = existing.copy(status = AppointmentStatus.CANCELLED)
        every { appointmentRepository.findById(1) } returns existing
        every { appointmentRepository.updateAppointmentStatus(1, AppointmentStatus.CANCELLED) } returns cancelled

        // when
        val result = cancelAppointmentByPatient.execute(1, requestingPatientId = 3)

        // then
        assertEquals(AppointmentStatus.CANCELLED, result.status)
        verify(exactly = 0) { userRepository.findById(any()) }
    }
}
