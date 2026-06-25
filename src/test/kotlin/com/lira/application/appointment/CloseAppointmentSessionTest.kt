package com.lira.application.appointment

import com.lira.domain.appointment.Appointment
import com.lira.domain.appointment.AppointmentRepository
import com.lira.domain.appointment.AppointmentStatus
import com.lira.domain.appointment.AppointmentType
import com.lira.domain.user.Patient
import com.lira.domain.user.Therapist
import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.time.LocalDateTime

class CloseAppointmentSessionTest {

    private lateinit var appointmentRepository: AppointmentRepository
    private lateinit var closeAppointmentSession: CloseAppointmentSession

    @BeforeEach
    fun setUp() {
        appointmentRepository = mockk()
        closeAppointmentSession = CloseAppointmentSession(appointmentRepository)
    }

    @Test
    fun `execute cambia status a COMPLETED y guarda las notas del terapeuta`() {
        // given
        val appointmentId = 10
        val notes = "El paciente mostró avance en la exposición"
        val existing = appointment(appointmentId, AppointmentStatus.CONFIRMED)
        val updatedSlot = slot<Appointment>()
        every { appointmentRepository.findById(appointmentId) } returns existing
        every { appointmentRepository.update(capture(updatedSlot)) } answers { updatedSlot.captured }

        // when
        closeAppointmentSession.execute(appointmentId, notes)

        // then
        val updated = updatedSlot.captured
        assertEquals(AppointmentStatus.COMPLETED, updated.status)
        assertEquals(notes, updated.therapistNotes)
        verify(exactly = 1) { appointmentRepository.update(any()) }
    }

    @Test
    fun `execute cierra cita sin notas cuando therapistNotes es null`() {
        // given
        val appointmentId = 11
        val existing = appointment(appointmentId, AppointmentStatus.CONFIRMED)
        val updatedSlot = slot<Appointment>()
        every { appointmentRepository.findById(appointmentId) } returns existing
        every { appointmentRepository.update(capture(updatedSlot)) } answers { updatedSlot.captured }

        // when
        closeAppointmentSession.execute(appointmentId, null)

        // then
        val updated = updatedSlot.captured
        assertEquals(AppointmentStatus.COMPLETED, updated.status)
        assertEquals(null, updated.therapistNotes)
    }

    private fun appointment(id: Int, status: AppointmentStatus) = Appointment(
        id = id,
        therapist = Therapist(id = 6),
        patient = Patient(id = 3),
        appointmentType = AppointmentType.FOLLOW_UP,
        appointmentDate = LocalDateTime.now(),
        appointmentDuration = 60,
        description = null,
        cost = BigDecimal("50.00"),
        status = status
    )
}
