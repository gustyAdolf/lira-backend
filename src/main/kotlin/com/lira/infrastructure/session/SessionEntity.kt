package com.lira.infrastructure.session

import com.lira.domain.session.Session
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "session")
data class SessionEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int = 0,

    @Column(name = "session_date", nullable = false)
    val sessionDate: LocalDateTime,

    @Column(name = "activation_level", nullable = false)
    val activationLevel: Int,

    @Column(name = "exposure_level", nullable = false)
    val exposureLevel: Int,

    @Column(name = "user_id", nullable = false)
    val userId: Int,

    @Column(name = "mental_disorder_id", nullable = false)
    val mentalDisorderId: Int,

    @Column(name = "progress", nullable = false)
    val progress: Int,

    @Column(name = "therapist_notes")
    val therapistNotes: String,

    @Column(name = "user_notes")
    val userNotes: String
)

fun SessionEntity.toDomain(): Session = Session(
    id = this.id,
    sessionDate = this.sessionDate,
    activationLevel = this.activationLevel,
    exposureLevel = this.exposureLevel,
    userId = this.userId,
    mentalDisorderId = this.mentalDisorderId,
    progress = this.progress,
    therapistNotes = this.therapistNotes,
    userNotes = this.userNotes
)
