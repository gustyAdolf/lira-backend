package com.lira.infrastructure.appointment.dto

import com.lira.domain.appointment.InitialAssessment
import java.time.LocalDateTime

data class InitialAssessmentResponse(
    val id: Int,
    val appointmentId: Int,
    val chiefComplaint: String?,
    val background: String?,
    val sessionNotes: String?,
    val nextSteps: String?,
    val transcript: String?,
    val audioLocalPath: String?,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
    val appointmentType: String?,
    val appointmentDate: LocalDateTime?,
)

fun InitialAssessment.toResponse() = InitialAssessmentResponse(
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
    appointmentType = appointmentType,
    appointmentDate = appointmentDate,
)
