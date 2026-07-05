package com.lira.application.appointment

import com.lira.domain.appointment.Appointment
import com.lira.domain.appointment.AppointmentRepository
import com.lira.domain.appointment.AppointmentType
import com.lira.domain.therapistschedule.TherapistSchedule
import com.lira.domain.therapistschedule.TherapistScheduleRepository
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
import org.springframework.data.domain.Sort
import java.math.BigDecimal
import java.time.LocalDateTime
import java.time.LocalTime

class CreateAppointmentTest {

    private lateinit var appointmentRepository: AppointmentRepository
    private lateinit var scheduleRepository: TherapistScheduleRepository
    private lateinit var createAppointment: CreateAppointment

    private val allDaySchedule = (1..7).map { dow ->
        TherapistSchedule(
            therapistId = 6,
            dayOfWeek = dow,
            startTime = LocalTime.of(8, 0),
            endTime = LocalTime.of(22, 0),
        )
    }

    @BeforeEach
    fun setUp() {
        appointmentRepository = mockk()
        scheduleRepository = mockk()
        every { scheduleRepository.findByTherapistId(any()) } returns allDaySchedule
        every { appointmentRepository.getTherapistAppointments(any(), any(), any(), any<Sort>()) } returns emptyList()
        createAppointment = CreateAppointment(appointmentRepository, scheduleRepository)
    }

    @Test
    fun `execute persiste la cita y devuelve el resultado del repositorio`() {
        // given
        val request = AppointmentRequest(
            therapistId = 6,
            userId = 3,
            appointmentType = AppointmentType.FOLLOW_UP,
            appointmentDate = LocalDateTime.of(2026, 7, 1, 10, 0),
            appointmentDuration = 60,
            cost = BigDecimal("25.00")
        )
        val savedAppointment = Appointment(
            id = 42,
            therapist = Therapist(id = 6),
            patient = Patient(id = 3),
            progressPlanId = null,
            appointmentType = AppointmentType.FOLLOW_UP,
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
        val planId = 5
        val request = AppointmentRequest(
            therapistId = 6,
            userId = 3,
            progressPlanId = planId,
            appointmentType = AppointmentType.FOLLOW_UP,
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
        assertEquals(planId, captured.progressPlanId)
        assertEquals(AppointmentType.FOLLOW_UP, captured.appointmentType)
    }

    @Test
    fun `execute crea cita sin plan para primera visita`() {
        // given
        val request = AppointmentRequest(
            therapistId = 6,
            userId = 3,
            appointmentType = AppointmentType.FIRST_VISIT,
            appointmentDate = LocalDateTime.now(),
            appointmentDuration = 60
        )
        val appointmentSlot = slot<Appointment>()
        every { appointmentRepository.save(capture(appointmentSlot)) } answers { appointmentSlot.captured }

        // when
        createAppointment.execute(request)

        // then
        val captured = appointmentSlot.captured
        assertEquals(AppointmentType.FIRST_VISIT, captured.appointmentType)
        assertEquals(null, captured.progressPlanId)
    }
}
