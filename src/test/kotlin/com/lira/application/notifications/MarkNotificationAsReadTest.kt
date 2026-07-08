package com.lira.application.notifications

import com.lira.domain.notifications.Notification
import com.lira.domain.notifications.NotificationRepository
import com.lira.domain.notifications.NotificationType
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.security.access.AccessDeniedException
import java.time.LocalDateTime

class MarkNotificationAsReadTest {

    private lateinit var notificationRepository: NotificationRepository
    private lateinit var markNotificationAsRead: MarkNotificationAsRead

    @BeforeEach
    fun setUp() {
        notificationRepository = mockk()
        markNotificationAsRead = MarkNotificationAsRead(notificationRepository)
    }

    @Test
    fun `execute marca como leida la notificacion del propio destinatario`() {
        // given
        val notification = Notification(
            id = 1,
            recipientUserId = 42,
            type = NotificationType.APPOINTMENT_CANCELLED,
            title = "Cita cancelada",
            body = "El paciente canceló la cita"
        )
        val readNotification = notification.copy(readAt = LocalDateTime.now())
        every { notificationRepository.findById(1) } returns notification
        every { notificationRepository.markAsRead(1) } returns readNotification

        // when
        val result = markNotificationAsRead.execute(1, requestingUserId = 42)

        // then
        verify(exactly = 1) { notificationRepository.markAsRead(1) }
        assertNotNull(result.readAt)
    }

    @Test
    fun `execute rechaza marcar como leida la notificacion de otro usuario`() {
        // given
        val notification = Notification(
            id = 1,
            recipientUserId = 42,
            type = NotificationType.APPOINTMENT_CANCELLED,
            title = "Cita cancelada",
            body = "El paciente canceló la cita"
        )
        every { notificationRepository.findById(1) } returns notification

        // when / then
        assertThrows<AccessDeniedException> {
            markNotificationAsRead.execute(1, requestingUserId = 99)
        }
        verify(exactly = 0) { notificationRepository.markAsRead(any()) }
    }

    @Test
    fun `execute es idempotente si ya estaba leida`() {
        // given
        val alreadyRead = Notification(
            id = 1,
            recipientUserId = 42,
            type = NotificationType.APPOINTMENT_CANCELLED,
            title = "Cita cancelada",
            body = "El paciente canceló la cita",
            readAt = LocalDateTime.now()
        )
        every { notificationRepository.findById(1) } returns alreadyRead

        // when
        val result = markNotificationAsRead.execute(1, requestingUserId = 42)

        // then
        verify(exactly = 0) { notificationRepository.markAsRead(any()) }
        assertEquals(alreadyRead, result)
    }
}
