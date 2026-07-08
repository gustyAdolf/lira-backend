package com.lira.application.notifications

import com.lira.domain.notifications.Notification
import com.lira.domain.notifications.NotificationRepository
import org.springframework.security.access.AccessDeniedException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class MarkNotificationAsRead(
    private val notificationRepository: NotificationRepository,
) {
    fun execute(notificationId: Int, requestingUserId: Int): Notification {
        val notification = notificationRepository.findById(notificationId)
        if (notification.recipientUserId != requestingUserId) {
            throw AccessDeniedException("No puedes marcar como leída la notificación de otro usuario")
        }
        if (notification.readAt != null) {
            return notification
        }
        return notificationRepository.markAsRead(notificationId)
    }
}
