package com.lira.infrastructure.notifications

import com.lira.domain.notifications.Notification
import com.lira.domain.notifications.NotificationType
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "notification")
data class NotificationEntity(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int = 0,

    @Column(name = "recipient_user_id")
    val recipientUserId: Int,

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    val type: NotificationType,

    @Column(name = "title")
    val title: String,

    @Column(name = "body")
    val body: String,

    @Column(name = "related_appointment_id")
    val relatedAppointmentId: Int? = null,

    @Column(name = "read_at")
    val readAt: LocalDateTime? = null,

    @Column(name = "created_at")
    val createdAt: LocalDateTime = LocalDateTime.now(),
)

fun NotificationEntity.toDomain(): Notification = Notification(
    id = id,
    recipientUserId = recipientUserId,
    type = type,
    title = title,
    body = body,
    relatedAppointmentId = relatedAppointmentId,
    readAt = readAt,
    createdAt = createdAt,
)

fun Notification.toEntity(): NotificationEntity = NotificationEntity(
    recipientUserId = recipientUserId,
    type = type,
    title = title,
    body = body,
    relatedAppointmentId = relatedAppointmentId,
    readAt = readAt,
    createdAt = createdAt,
)
