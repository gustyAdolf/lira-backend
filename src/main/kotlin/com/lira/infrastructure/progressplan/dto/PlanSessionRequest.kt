package com.lira.infrastructure.progressplan.dto

import com.lira.domain.progressplan.PlanSession
import java.time.LocalDateTime

data class PlanSessionRequest(
    val planId: Int,
    val therapistId: Int,
    val appointmentId: Int? = null,
    val notes: String? = null,
    val aiSummary: String? = null,
)

fun PlanSessionRequest.toDomain() = PlanSession(
    planId = planId,
    therapistId = therapistId,
    appointmentId = appointmentId,
    sessionDate = LocalDateTime.now(),
    notes = notes,
    aiSummary = aiSummary,
)
