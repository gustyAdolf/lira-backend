package com.lira.domain.appointment

import java.time.LocalDateTime

data class InitialAssessment(
    val id: Int = 0,
    val appointmentId: Int,
    val chiefComplaint: String? = null,
    val background: String? = null,
    val sessionNotes: String? = null,
    val nextSteps: String? = null,
    val transcript: String? = null,
    val audioLocalPath: String? = null,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now(),
    // Context fields — only populated in patient-scoped list queries
    val appointmentType: String? = null,
    val appointmentDate: LocalDateTime? = null,
)
