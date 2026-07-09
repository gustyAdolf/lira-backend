package com.lira.infrastructure.notifications

import com.lira.domain.notifications.NotificationType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface JpaNotificationRepository : JpaRepository<NotificationEntity, Int> {

    fun findByRecipientUserIdOrderByCreatedAtDesc(recipientUserId: Int): List<NotificationEntity>

    @Query(
        """SELECT COUNT(n) > 0 FROM NotificationEntity n
            WHERE n.relatedAppointmentId = :appointmentId AND n.type = :type"""
    )
    fun existsForAppointmentAndType(
        @Param("appointmentId") appointmentId: Int,
        @Param("type") type: NotificationType
    ): Boolean
}
