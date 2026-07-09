package com.lira.application.appointment

import com.lira.domain.appointment.Appointment
import com.lira.domain.appointment.AppointmentRepository
import com.lira.domain.appointment.AppointmentStatus
import com.lira.domain.appointment.AppointmentType
import com.lira.domain.exceptions.AppointmentException
import com.lira.domain.user.Patient
import com.lira.domain.user.Therapist
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.security.access.AccessDeniedException
import java.math.BigDecimal
import java.time.LocalDateTime

class ConfirmAppointmentAttendanceTest {

    private lateinit var appointmentRepository: AppointmentRepository
    private lateinit var confirmAppointmentAttendance: ConfirmAppointmentAttendance

    private fun appointment(
        id: Int = 1,
        patientId: Int = 3,
        status: AppointmentStatus = AppointmentStatus.PENDING
    ) = Appointment(
        id = id,
        therapist = Therapist(id = 6),
        patient = Patient(id = patientId),
        appointmentType = AppointmentType.GENERAL,
        appointmentDate = LocalDateTime.now().plusDays(3),
        appointmentDuration = 60,
        description = null,
        cost = BigDecimal("25.00"),
        status = status
    )

    @BeforeEach
    fun setUp() {
        appointmentRepository = mockk()
        confirmAppointmentAttendance = ConfirmAppointmentAttendance(appointmentRepository)
    }

    @Test
    fun `execute permite al paciente dueno confirmar su asistencia`() {
        // given
        val existing = appointment()
        val confirmed = existing.copy(patientConfirmedAt = LocalDateTime.now())
        every { appointmentRepository.findById(1) } returns existing
        every { appointmentRepository.confirmAttendance(1) } returns confirmed

        // when
        val result = confirmAppointmentAttendance.execute(1, requestingPatientId = 3)

        // then
        verify(exactly = 1) { appointmentRepository.confirmAttendance(1) }
        assertNotNull(result.patientConfirmedAt)
    }

    @Test
    fun `execute rechaza confirmar la cita de otro paciente`() {
        // given
        every { appointmentRepository.findById(1) } returns appointment(patientId = 3)

        // when / then
        assertThrows<AccessDeniedException> {
            confirmAppointmentAttendance.execute(1, requestingPatientId = 99)
        }
        verify(exactly = 0) { appointmentRepository.confirmAttendance(any()) }
    }

    @Test
    fun `execute rechaza confirmar una cita cancelada`() {
        // given
        every { appointmentRepository.findById(1) } returns appointment(status = AppointmentStatus.CANCELLED)

        // when / then
        assertThrows<AppointmentException.NotEditable> {
            confirmAppointmentAttendance.execute(1, requestingPatientId = 3)
        }
        verify(exactly = 0) { appointmentRepository.confirmAttendance(any()) }
    }
}
