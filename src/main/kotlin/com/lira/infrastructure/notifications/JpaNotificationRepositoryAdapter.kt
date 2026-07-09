package com.lira.infrastructure.notifications

import com.lira.domain.exceptions.NotificationException
import com.lira.domain.notifications.Notification
import com.lira.domain.notifications.NotificationRepository
import com.lira.domain.notifications.NotificationType
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
class JpaNotificationRepositoryAdapter(
    private val jpaNotificationRepository: JpaNotificationRepository
) : NotificationRepository {

    override fun save(notification: Notification): Notification =
        jpaNotificationRepository.save(notification.toEntity()).toDomain()

    override fun findByRecipient(recipientUserId: Int): List<Notification> =
        jpaNotificationRepository.findByRecipientUserIdOrderByCreatedAtDesc(recipientUserId)
            .map { it.toDomain() }

    override fun findById(id: Int): Notification =
        jpaNotificationRepository.findById(id)
            .orElseThrow { NotificationException.NoNotificationExists() }
            .toDomain()

    override fun markAsRead(id: Int): Notification {
        val entity = jpaNotificationRepository.findById(id)
            .orElseThrow { NotificationException.NoNotificationExists() }
        val updated = entity.copy(readAt = LocalDateTime.now())
        return jpaNotificationRepository.save(updated).toDomain()
    }

    override fun existsForAppointmentAndType(appointmentId: Int, type: NotificationType): Boolean =
        jpaNotificationRepository.existsForAppointmentAndType(appointmentId, type)
}
