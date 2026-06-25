package com.lira.infrastructure.progressplan.dto

import com.fasterxml.jackson.annotation.JsonFormat
import com.lira.domain.progressplan.PlanSession
import com.lira.domain.progressplan.SubobjectiveEntry
import java.time.LocalDateTime

data class PlanSessionResponse(
    val id: Int,
    val planId: Int,
    val therapistId: Int,
    val appointmentId: Int?,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    val sessionDate: LocalDateTime,
    val notes: String?,
    val entries: List<SubobjectiveEntryResponse>
)

fun PlanSession.toResponse(entries: List<SubobjectiveEntry> = emptyList()) = PlanSessionResponse(
    id = id,
    planId = planId,
    therapistId = therapistId,
    appointmentId = appointmentId,
    sessionDate = sessionDate,
    notes = notes,
    entries = entries.map { it.toEntryResponse() }
)
