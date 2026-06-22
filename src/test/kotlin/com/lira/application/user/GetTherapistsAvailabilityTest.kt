package com.lira.application.user

import com.lira.domain.appointment.AppointmentRepository
import com.lira.domain.user.Therapist
import com.lira.domain.user.UserRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.data.domain.Pageable
import java.time.LocalDateTime

class GetTherapistsAvailabilityTest {

    private lateinit var userRepository: UserRepository
    private lateinit var appointmentRepository: AppointmentRepository
    private lateinit var getTherapistsAvailability: GetTherapistsAvailability

    @BeforeEach
    fun setUp() {
        userRepository = mockk()
        appointmentRepository = mockk()
        getTherapistsAvailability = GetTherapistsAvailability(userRepository, appointmentRepository)
    }

    @Test
    fun `execute ordena terapeutas de menor a mayor carga de citas`() {
        // given
        val therapistWithLoad = Therapist(id = 1, name = "Ocupado", email = "busy@lira.com")
        val therapistFree = Therapist(id = 2, name = "Libre", email = "free@lira.com")
        val companyId = 14

        every { userRepository.findTherapistsByCompanyId(companyId) } returns
                listOf(therapistWithLoad, therapistFree)
        every {
            appointmentRepository.countAppointmentsByTherapists(any(), any<LocalDateTime>())
        } returns mapOf(1 to 3, 2 to 0)
        every {
            appointmentRepository.findNextAppointmentsForTherapist(any(), any<Pageable>())
        } returns emptyList()

        // when
        val result = getTherapistsAvailability.execute(companyId)

        // then
        assertEquals(2, result.size)
        assertEquals(0, result.first().futureAppointmentsCount)
        assertEquals(2, result.first().therapistId)
        assertEquals(3, result.last().futureAppointmentsCount)
    }

    @Test
    fun `execute devuelve lista vacia si la empresa no tiene terapeutas`() {
        // given
        val companyId = 99
        every { userRepository.findTherapistsByCompanyId(companyId) } returns emptyList()

        // when
        val result = getTherapistsAvailability.execute(companyId)

        // then
        assertTrue(result.isEmpty())
        verify(exactly = 0) {
            appointmentRepository.countAppointmentsByTherapists(any<List<Int>>(), any<LocalDateTime>())
        }
    }
}
