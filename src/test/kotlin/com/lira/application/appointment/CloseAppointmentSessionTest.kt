package com.lira.application.appointment

import com.lira.domain.appointment.Appointment
import com.lira.domain.appointment.AppointmentRepository
import com.lira.domain.appointment.AppointmentStatus
import com.lira.domain.appointment.AppointmentType
import com.lira.domain.progressplan.PlanSession
import com.lira.domain.progressplan.PlanSessionRepository
import com.lira.domain.user.Patient
import com.lira.domain.user.Therapist
import io.mockk.every
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
    private lateinit var planSessionRepository: PlanSessionRepository
    private lateinit var closeAppointmentSession: CloseAppointmentSession

    @BeforeEach
    fun setUp() {
        appointmentRepository = mockk()
        planSessionRepository = mockk()
        closeAppointmentSession = CloseAppointmentSession(appointmentRepository, planSessionRepository)
    }

    @Test
    fun `execute cambia status a COMPLETED y guarda las notas del terapeuta`() {
        // given
        val appointmentId = 10
        val notes = "El paciente mostró avance en la exposición"
        val existing = appointment(appointmentId, AppointmentStatus.CONFIRMED, progressPlanId = 1)
        val updatedSlot = slot<Appointment>()
        val sessionSlot = slot<PlanSession>()
        every { appointmentRepository.findById(appointmentId) } returns existing
        every { appointmentRepository.update(capture(updatedSlot)) } answers { updatedSlot.captured }
        every { planSessionRepository.save(capture(sessionSlot)) } answers { sessionSlot.captured.copy(id = 99) }

        // when
        closeAppointmentSession.execute(appointmentId, notes)

        // then
        assertEquals(AppointmentStatus.COMPLETED, updatedSlot.captured.status)
        assertEquals(notes, updatedSlot.captured.therapistNotes)
        assertEquals(appointmentId, sessionSlot.captured.appointmentId)
        assertEquals(1, sessionSlot.captured.planId)
        assertEquals(notes, sessionSlot.captured.notes)
        verify(exactly = 1) { planSessionRepository.save(any()) }
    }

    @Test
    fun `execute no crea PlanSession si la cita no tiene plan asociado`() {
        // given
        val appointmentId = 11
        val existing = appointment(appointmentId, AppointmentStatus.CONFIRMED, progressPlanId = null)
        val updatedSlot = slot<Appointment>()
        every { appointmentRepository.findById(appointmentId) } returns existing
        every { appointmentRepository.update(capture(updatedSlot)) } answers { updatedSlot.captured }

        // when
        closeAppointmentSession.execute(appointmentId, null)

        // then
        assertEquals(AppointmentStatus.COMPLETED, updatedSlot.captured.status)
        verify(exactly = 0) { planSessionRepository.save(any()) }
    }

    private fun appointment(id: Int, status: AppointmentStatus, progressPlanId: Int?) = Appointment(
        id = id,
        therapist = Therapist(id = 6),
        patient = Patient(id = 3),
        appointmentType = AppointmentType.FOLLOW_UP,
        progressPlanId = progressPlanId,
        appointmentDate = LocalDateTime.now(),
        appointmentDuration = 60,
        description = null,
        cost = BigDecimal("50.00"),
        status = status
    )
}
