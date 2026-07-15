package com.lira.application.checkin

import com.lira.domain.checkin.Checkin
import com.lira.domain.checkin.CheckinRepository
import com.lira.domain.exceptions.CheckinException
import com.lira.domain.user.Therapist
import com.lira.domain.user.UserRepository
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.security.access.AccessDeniedException
import java.time.LocalDate

class GetCompanyTherapistCheckinsTest {

    private lateinit var checkinRepository: CheckinRepository
    private lateinit var userRepository: UserRepository
    private lateinit var getCompanyTherapistCheckins: GetCompanyTherapistCheckins

    private val companyId = 1
    private val therapistId = 42
    private val from = LocalDate.now().minusDays(10)
    private val to = LocalDate.now()

    @BeforeEach
    fun setUp() {
        checkinRepository = mockk()
        userRepository = mockk()
        getCompanyTherapistCheckins = GetCompanyTherapistCheckins(checkinRepository, userRepository)
    }

    @Test
    fun `execute rechaza si el rango de fechas no se especifica`() {
        assertThrows<CheckinException.InvalidDateRangeException> {
            getCompanyTherapistCheckins.execute(companyId, therapistId, null, to)
        }
    }

    @Test
    fun `execute rechaza si el rango supera los 366 dias`() {
        assertThrows<CheckinException.InvalidDateRangeException> {
            getCompanyTherapistCheckins.execute(companyId, therapistId, from.minusYears(2), to)
        }
    }

    @Test
    fun `execute rechaza un terapeuta que no pertenece a la clinica`() {
        every { userRepository.findTherapistsByCompanyId(companyId) } returns listOf(
            Therapist(id = 99, companyId = companyId)
        )

        assertThrows<AccessDeniedException> {
            getCompanyTherapistCheckins.execute(companyId, therapistId, from, to)
        }
    }

    @Test
    fun `execute calcula horas totales, media diaria e incidencias del terapeuta de la clinica`() {
        every { userRepository.findTherapistsByCompanyId(companyId) } returns listOf(
            Therapist(id = therapistId, companyId = companyId)
        )
        val day1 = LocalDate.now().minusDays(2)
        val day2 = LocalDate.now().minusDays(1)
        val checkins = listOf(
            Checkin(
                id = 1, userId = therapistId,
                checkinTime = day1.atTime(9, 0), checkoutTime = day1.atTime(17, 0), totalHours = 8.0
            ),
            Checkin(
                id = 2, userId = therapistId,
                checkinTime = day2.atTime(9, 0), checkoutTime = day2.atTime(23, 59, 59),
                totalHours = 14.98, autoClosed = true
            ),
        )
        every { checkinRepository.getHistory(therapistId, from, to, companyId) } returns checkins

        val result = getCompanyTherapistCheckins.execute(companyId, therapistId, from, to)

        assertEquals(22.98, result.totalHours, 0.001)
        assertEquals(11.49, result.averageDailyHours, 0.001)
        assertEquals(1, result.incidentCount)
    }
}
