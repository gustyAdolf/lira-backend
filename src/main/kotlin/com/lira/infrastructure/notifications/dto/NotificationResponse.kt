package com.lira.infrastructure.notifications.dto

import com.fasterxml.jackson.annotation.JsonFormat
import com.lira.domain.notifications.Notification
import com.lira.domain.notifications.NotificationType
import java.time.LocalDateTime

data class NotificationResponse(
    val id: Int,
    val type: NotificationType,
    val title: String,
    val body: String,
    val relatedAppointmentId: Int?,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    val readAt: LocalDateTime?,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    val createdAt: LocalDateTime,
)

fun Notification.toResponse(): NotificationResponse = NotificationResponse(
    id = id,
    type = type,
    title = title,
    body = body,
    relatedAppointmentId = relatedAppointmentId,
    readAt = readAt,
    createdAt = createdAt,
)
