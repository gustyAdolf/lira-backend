package com.lira.domain.progressplan

import java.time.LocalDateTime

data class PlanSession(
    val id: Int = 0,
    val planId: Int,
    val therapistId: Int,
    val appointmentId: Int? = null,
    val sessionDate: LocalDateTime = LocalDateTime.now(),
    val notes: String? = null,
    val transcript: String? = null,
    val aiSummary: String? = null,
    val createdAt: LocalDateTime = LocalDateTime.now()
)
