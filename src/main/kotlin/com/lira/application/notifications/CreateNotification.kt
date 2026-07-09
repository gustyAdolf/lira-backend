package com.lira.application.notifications

import com.lira.domain.notifications.Notification
import com.lira.domain.notifications.NotificationRepository
import com.lira.domain.notifications.NotificationType
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class CreateNotification(
    private val notificationRepository: NotificationRepository,
) {
    fun execute(
        recipientUserId: Int,
        type: NotificationType,
        title: String,
        body: String,
        relatedAppointmentId: Int? = null,
    ): Notification = notificationRepository.save(
        Notification(
            recipientUserId = recipientUserId,
            type = type,
            title = title,
            body = body,
            relatedAppointmentId = relatedAppointmentId,
        )
    )
}
