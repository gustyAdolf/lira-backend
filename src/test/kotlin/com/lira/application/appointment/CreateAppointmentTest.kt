package com.lira.application.appointment

import com.lira.domain.appointment.Appointment
import com.lira.domain.appointment.AppointmentRepository
import com.lira.domain.mentaldisorder.MentalDisorder
import com.lira.domain.user.Patient
import com.lira.domain.user.Therapist
import com.lira.domain.appointment.AppointmentStatus
import com.lira.infrastructure.appointment.dto.AppointmentRequest
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.time.LocalDateTime

class CreateAppointmentTest {

    private lateinit var appointmentRepository: AppointmentRepository
    private lateinit var createAppointment: CreateAppointment

    @BeforeEach
    fun setUp() {
        appointmentRepository = mockk()
        createAppointment = CreateAppointment(appointmentRepository)
    }

    @Test
    fun `execute persiste la cita y devuelve el resultado del repositorio`() {
        // given
        val request = AppointmentRequest(
            therapistId = 6,
            userId = 3,
            mentalDisorderId = 1,
            appointmentDate = LocalDateTime.of(2026, 7, 1, 10, 0),
            appointmentDuration = 60,
            cost = BigDecimal("25.00")
        )
        val savedAppointment = Appointment(
            id = 42,
            therapist = Therapist(id = 6),
            patient = Patient(id = 3),
            mentalDisorder = MentalDisorder(id = 1),
            appointmentDate = request.appointmentDate,
            appointmentDuration = 60,
            description = null,
            cost = BigDecimal("25.00"),
            status = AppointmentStatus.PENDING
        )
        every { appointmentRepository.save(any()) } returns savedAppointment

        // when
        val result = createAppointment.execute(request)

        // then
        verify(exactly = 1) { appointmentRepository.save(any()) }
        assertEquals(savedAppointment, result)
    }

    @Test
    fun `execute mapea los ids del request correctamente al dominio`() {
        // given
        val request = AppointmentRequest(
            therapistId = 6,
            userId = 3,
            mentalDisorderId = 1,
            appointmentDate = LocalDateTime.now(),
            appointmentDuration = 60
        )
        val appointmentSlot = slot<Appointment>()
        every { appointmentRepository.save(capture(appointmentSlot)) } answers { appointmentSlot.captured }

        // when
        createAppointment.execute(request)

        // then
        val captured = appointmentSlot.captured
        assertEquals(6, captured.therapist.id)
        assertEquals(3, captured.patient.id)
        assertEquals(1, captured.mentalDisorder.id)
    }
}
