package com.lira.application.appointment

import com.lira.domain.appointment.Appointment
import com.lira.domain.appointment.AppointmentRepository
import com.lira.domain.appointment.AppointmentType
import com.lira.domain.exceptions.AppointmentException
import com.lira.domain.user.Patient
import com.lira.domain.user.Therapist
import com.lira.domain.appointment.AppointmentStatus
import com.lira.infrastructure.appointment.dto.UpdateAppointmentStatusRequest
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.math.BigDecimal
import java.time.LocalDateTime

class UpdateAppointmentStatusTest {

    private lateinit var appointmentRepository: AppointmentRepository
    private lateinit var updateAppointmentStatus: UpdateAppointmentStatus

    @BeforeEach
    fun setUp() {
        appointmentRepository = mockk()
        updateAppointmentStatus = UpdateAppointmentStatus(appointmentRepository)
    }

    @Test
    fun `execute actualiza el estado a CONFIRMED y devuelve la cita actualizada`() {
        // given
        val appointmentId = 1
        val request = UpdateAppointmentStatusRequest(status = AppointmentStatus.CONFIRMED)
        val updatedAppointment = Appointment(
            id = appointmentId,
            therapist = Therapist(id = 6),
            patient = Patient(id = 3),
            appointmentType = AppointmentType.GENERAL,
            appointmentDate = LocalDateTime.now(),
            appointmentDuration = 60,
            description = null,
            cost = BigDecimal("25.00"),
            status = AppointmentStatus.CONFIRMED
        )
        every {
            appointmentRepository.updateAppointmentStatus(appointmentId, AppointmentStatus.CONFIRMED)
        } returns updatedAppointment

        // when
        val result = updateAppointmentStatus.execute(appointmentId, request)

        // then
        verify(exactly = 1) {
            appointmentRepository.updateAppointmentStatus(appointmentId, AppointmentStatus.CONFIRMED)
        }
        assertEquals(AppointmentStatus.CONFIRMED, result.status)
    }

    @Test
    fun `execute propaga excepcion cuando la cita no existe`() {
        // given
        val unknownId = 999
        val request = UpdateAppointmentStatusRequest(status = AppointmentStatus.CONFIRMED)
        every {
            appointmentRepository.updateAppointmentStatus(unknownId, any())
        } throws AppointmentException.NoAppointmentExists()

        // when / then
        assertThrows<AppointmentException.NoAppointmentExists> {
            updateAppointmentStatus.execute(unknownId, request)
        }
    }
}
