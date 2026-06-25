package com.lira.infrastructure.progressplan.entity

import com.lira.domain.progressplan.PlanSession
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "plan_session")
class PlanSessionEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int = 0,

    @Column(name = "plan_id", nullable = false)
    val planId: Int,

    @Column(name = "therapist_id", nullable = false)
    val therapistId: Int,

    @Column(name = "appointment_id")
    val appointmentId: Int? = null,

    @Column(name = "session_date", nullable = false)
    val sessionDate: LocalDateTime = LocalDateTime.now(),

    @Column(name = "notes")
    val notes: String? = null,

    @Column(name = "created_at", nullable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),
)

fun PlanSessionEntity.toDomain() = PlanSession(
    id = id,
    planId = planId,
    therapistId = therapistId,
    appointmentId = appointmentId,
    sessionDate = sessionDate,
    notes = notes,
    createdAt = createdAt
)

fun PlanSession.toEntity() = PlanSessionEntity(
    planId = planId,
    therapistId = therapistId,
    appointmentId = appointmentId,
    sessionDate = sessionDate,
    notes = notes,
    createdAt = createdAt
)
