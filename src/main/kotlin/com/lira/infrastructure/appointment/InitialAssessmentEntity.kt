package com.lira.infrastructure.appointment

import com.lira.domain.appointment.InitialAssessment
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "initial_assessment")
data class InitialAssessmentEntity(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int = 0,

    @Column(name = "appointment_id", unique = true, nullable = false)
    val appointmentId: Int,

    @Column(name = "chief_complaint", columnDefinition = "TEXT")
    val chiefComplaint: String? = null,

    @Column(name = "background", columnDefinition = "TEXT")
    val background: String? = null,

    @Column(name = "session_notes", columnDefinition = "TEXT")
    val sessionNotes: String? = null,

    @Column(name = "next_steps", columnDefinition = "TEXT")
    val nextSteps: String? = null,

    @Column(name = "transcript", columnDefinition = "TEXT")
    val transcript: String? = null,

    @Column(name = "audio_local_path")
    val audioLocalPath: String? = null,

    @Column(name = "created_at")
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @Column(name = "updated_at")
    val updatedAt: LocalDateTime = LocalDateTime.now(),
)

fun InitialAssessmentEntity.toDomain() = InitialAssessment(
    id = id,
    appointmentId = appointmentId,
    chiefComplaint = chiefComplaint,
    background = background,
    sessionNotes = sessionNotes,
    nextSteps = nextSteps,
    transcript = transcript,
    audioLocalPath = audioLocalPath,
    createdAt = createdAt,
    updatedAt = updatedAt,
)

fun InitialAssessment.toEntity() = InitialAssessmentEntity(
    id = id,
    appointmentId = appointmentId,
    chiefComplaint = chiefComplaint,
    background = background,
    sessionNotes = sessionNotes,
    nextSteps = nextSteps,
    transcript = transcript,
    audioLocalPath = audioLocalPath,
    createdAt = createdAt,
    updatedAt = updatedAt,
)
