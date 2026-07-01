package com.lira.infrastructure.progressplan.dto

import com.lira.domain.progressplan.TranscriptionResult

data class TranscriptionResponse(
    val transcript: String,
    val aiSummary: String?
)

fun TranscriptionResult.toResponse() = TranscriptionResponse(
    transcript = transcript,
    aiSummary = aiSummary
)
