package com.lira.infrastructure.appointment.dto

import com.lira.application.appointment.InitialAssessmentPatch

data class InitialAssessmentRequest(
    val chiefComplaint: String? = null,
    val background: String? = null,
    val sessionNotes: String? = null,
    val nextSteps: String? = null,
    val transcript: String? = null,
    val audioLocalPath: String? = null,
)

fun InitialAssessmentRequest.toPatch() = InitialAssessmentPatch(
    chiefComplaint = chiefComplaint,
    background = background,
    sessionNotes = sessionNotes,
    nextSteps = nextSteps,
    transcript = transcript,
    audioLocalPath = audioLocalPath,
)
