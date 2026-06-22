package com.lira.domain.progressplan

import com.lira.domain.user.Patient
import com.lira.domain.user.Therapist
import java.time.LocalDateTime

data class ProgressPlan(
    val id: Int = 0,
    val patient: Patient,
    val therapist: Therapist,
    val title: String,
    var totalProgress: Double = 0.0,
    val description: String?,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now(),
    val objectives: List<Objective>
)

