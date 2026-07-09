package com.lira.application.notifications

import com.lira.domain.notifications.Notification
import com.lira.domain.notifications.NotificationRepository
import org.springframework.stereotype.Service

@Service
class GetUserNotifications(
    private val notificationRepository: NotificationRepository,
) {
    fun execute(userId: Int): List<Notification> = notificationRepository.findByRecipient(userId)
}
