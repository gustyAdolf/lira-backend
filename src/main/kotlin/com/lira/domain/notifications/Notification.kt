package com.lira.domain.notifications

import java.time.LocalDateTime

data class Notification(
    val id: Int = 0,
    val recipientUserId: Int,
    val type: NotificationType,
    val title: String,
    val body: String,
    val relatedAppointmentId: Int? = null,
    val readAt: LocalDateTime? = null,
    val createdAt: LocalDateTime = LocalDateTime.now(),
)
