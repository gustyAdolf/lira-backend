package com.lira.application.appointment

import com.lira.domain.appointment.Appointment
import com.lira.domain.appointment.AppointmentRepository
import com.lira.domain.appointment.AppointmentStatus
import com.lira.domain.appointment.AppointmentType
import com.lira.domain.user.Patient
import com.lira.domain.user.Therapist
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.time.LocalDateTime

class GetAppointmentsByPlanTest {

    private lateinit var appointmentRepository: AppointmentRepository
    private lateinit var getAppointmentsByPlan: GetAppointmentsByPlan

    @BeforeEach
    fun setUp() {
        appointmentRepository = mockk()
        getAppointmentsByPlan = GetAppointmentsByPlan(appointmentRepository)
    }

    @Test
    fun `execute devuelve las citas completadas del plan`() {
        // given
        val planId = 5
        val appointments = listOf(
            completedAppointment(id = 3, planId = planId, date = LocalDateTime.of(2026, 6, 20, 10, 0)),
            completedAppointment(id = 1, planId = planId, date = LocalDateTime.of(2026, 6, 10, 10, 0)),
        )
        every { appointmentRepository.findCompletedByPlanId(planId) } returns appointments

        // when
        val result = getAppointmentsByPlan.execute(planId)

        // then
        assertEquals(2, result.size)
        assertEquals(3, result[0].id)
        assertEquals(1, result[1].id)
        verify(exactly = 1) { appointmentRepository.findCompletedByPlanId(planId) }
    }

    @Test
    fun `execute devuelve lista vacía cuando el plan no tiene citas completadas`() {
        // given
        val planId = 99
        every { appointmentRepository.findCompletedByPlanId(planId) } returns emptyList()

        // when
        val result = getAppointmentsByPlan.execute(planId)

        // then
        assertEquals(0, result.size)
    }

    private fun completedAppointment(id: Int, planId: Int, date: LocalDateTime) = Appointment(
        id = id,
        therapist = Therapist(id = 6),
        patient = Patient(id = 3),
        appointmentType = AppointmentType.FOLLOW_UP,
        progressPlanId = planId,
        appointmentDate = date,
        appointmentDuration = 60,
        description = null,
        cost = BigDecimal("50.00"),
        status = AppointmentStatus.COMPLETED
    )
}
