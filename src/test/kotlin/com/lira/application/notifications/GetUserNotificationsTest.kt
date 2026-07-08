package com.lira.application.notifications

import com.lira.domain.notifications.Notification
import com.lira.domain.notifications.NotificationRepository
import com.lira.domain.notifications.NotificationType
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class GetUserNotificationsTest {

    private lateinit var notificationRepository: NotificationRepository
    private lateinit var getUserNotifications: GetUserNotifications

    @BeforeEach
    fun setUp() {
        notificationRepository = mockk()
        getUserNotifications = GetUserNotifications(notificationRepository)
    }

    @Test
    fun `execute devuelve solo las notificaciones del usuario solicitante`() {
        // given
        val userId = 42
        val notifications = listOf(
            Notification(
                id = 1,
                recipientUserId = userId,
                type = NotificationType.APPOINTMENT_CANCELLED,
                title = "Cita cancelada",
                body = "El paciente canceló la cita"
            )
        )
        every { notificationRepository.findByRecipient(userId) } returns notifications

        // when
        val result = getUserNotifications.execute(userId)

        // then
        verify(exactly = 1) { notificationRepository.findByRecipient(userId) }
        assertEquals(notifications, result)
    }
}
