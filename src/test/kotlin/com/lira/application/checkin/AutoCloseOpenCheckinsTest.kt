package com.lira.application.checkin

import com.lira.application.notifications.CreateNotification
import com.lira.domain.checkin.Checkin
import com.lira.domain.checkin.CheckinRepository
import com.lira.domain.notifications.Notification
import com.lira.domain.notifications.NotificationType
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import java.time.LocalTime

class AutoCloseOpenCheckinsTest {

    private lateinit var checkinRepository: CheckinRepository
    private lateinit var createNotification: CreateNotification
    private lateinit var autoCloseOpenCheckins: AutoCloseOpenCheckins

    @BeforeEach
    fun setUp() {
        checkinRepository = mockk()
        createNotification = mockk()
        autoCloseOpenCheckins = AutoCloseOpenCheckins(checkinRepository, createNotification)
    }

    @Test
    fun `cierra cada sesion abierta de dias anteriores con la hora de fin de ese dia y notifica`() {
        val checkinTime = LocalDateTime.now().minusDays(2).withHour(9).withMinute(0).withSecond(0).withNano(0)
        val open = Checkin(id = 5, userId = 42, checkinTime = checkinTime)
        every { checkinRepository.findOpenFromPreviousDays() } returns listOf(open)
        every { checkinRepository.autoClose(5, any()) } just runs
        every {
            createNotification.execute(
                recipientUserId = 42,
                type = NotificationType.WORKDAY_AUTO_CLOSED,
                title = any(),
                body = any(),
            )
        } returns mockk<Notification>()

        autoCloseOpenCheckins.execute()

        val expectedEnd = LocalDateTime.of(checkinTime.toLocalDate(), LocalTime.of(23, 59, 59))
        verify(exactly = 1) { checkinRepository.autoClose(5, expectedEnd) }
        verify(exactly = 1) {
            createNotification.execute(
                recipientUserId = 42,
                type = NotificationType.WORKDAY_AUTO_CLOSED,
                title = any(),
                body = any(),
            )
        }
    }

    @Test
    fun `no hace nada si no hay sesiones abiertas de dias anteriores`() {
        every { checkinRepository.findOpenFromPreviousDays() } returns emptyList()

        autoCloseOpenCheckins.execute()

        verify(exactly = 0) { checkinRepository.autoClose(any(), any()) }
    }

    @Test
    fun `cierra varias sesiones abiertas de dias distintos de forma independiente`() {
        val checkinA = Checkin(id = 1, userId = 10, checkinTime = LocalDateTime.now().minusDays(3).withHour(8))
        val checkinB = Checkin(id = 2, userId = 20, checkinTime = LocalDateTime.now().minusDays(1).withHour(10))
        every { checkinRepository.findOpenFromPreviousDays() } returns listOf(checkinA, checkinB)
        every { checkinRepository.autoClose(any(), any()) } just runs
        every {
            createNotification.execute(any(), NotificationType.WORKDAY_AUTO_CLOSED, any(), any())
        } returns mockk<Notification>()

        autoCloseOpenCheckins.execute()

        verify(exactly = 1) { checkinRepository.autoClose(1, any()) }
        verify(exactly = 1) { checkinRepository.autoClose(2, any()) }
        verify(exactly = 1) { createNotification.execute(10, NotificationType.WORKDAY_AUTO_CLOSED, any(), any()) }
        verify(exactly = 1) { createNotification.execute(20, NotificationType.WORKDAY_AUTO_CLOSED, any(), any()) }
    }
}
