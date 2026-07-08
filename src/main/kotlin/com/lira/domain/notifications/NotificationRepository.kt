package com.lira.domain.notifications

interface NotificationRepository {

    fun save(notification: Notification): Notification

    fun findByRecipient(recipientUserId: Int): List<Notification>

    fun findById(id: Int): Notification

    fun markAsRead(id: Int): Notification

    fun existsForAppointmentAndType(appointmentId: Int, type: NotificationType): Boolean
}
