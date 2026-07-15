package com.lira.application.checkin

import com.lira.domain.checkin.Checkin
import com.lira.domain.checkin.CheckinRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.time.LocalDateTime

class GetCheckinHistoryTest {

    private lateinit var checkinRepository: CheckinRepository
    private lateinit var getCheckinHistory: GetCheckinHistory

    @BeforeEach
    fun setUp() {
        checkinRepository = mockk()
        getCheckinHistory = GetCheckinHistory(checkinRepository)
    }

    @Test
    fun `execute sin rango delega en el repositorio sin filtro de fechas`() {
        val userId = 7
        val history = listOf(Checkin(id = 1, userId = userId, checkinTime = LocalDateTime.now()))
        every { checkinRepository.getHistory(userId, null, null) } returns history

        val result = getCheckinHistory.execute(userId)

        verify(exactly = 1) { checkinRepository.getHistory(userId, null, null) }
        assertEquals(history, result)
    }

    @Test
    fun `execute con rango delega el filtro de fechas en el repositorio`() {
        val userId = 7
        val from = LocalDate.now().minusDays(30)
        val to = LocalDate.now()
        every { checkinRepository.getHistory(userId, from, to) } returns emptyList()

        getCheckinHistory.execute(userId, from, to)

        verify(exactly = 1) { checkinRepository.getHistory(userId, from, to) }
    }
}
