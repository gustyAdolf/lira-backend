package com.lira.infrastructure.progressplan.dto

import com.fasterxml.jackson.annotation.JsonFormat
import com.lira.domain.progressplan.Objective
import com.lira.domain.progressplan.ProgressPlan
import com.lira.domain.user.Patient
import com.lira.domain.user.Therapist
import java.time.LocalDateTime

data class ProgressPlanResponse(
    val id: Int,
    val patient: Patient,
    val therapist: Therapist,
    val title: String,
    val description: String?,
    val totalProgress: Double = 0.0,
    @field:JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    val createdAt: LocalDateTime,
    @field:JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    val updatedAt: LocalDateTime,
    val objectives: List<ObjectiveResponse>
)

fun ProgressPlan.toResponse(): ProgressPlanResponse {
    return ProgressPlanResponse(
        id = id,
        patient = patient,
        therapist = therapist,
        title = title,
        description = description,
        createdAt = createdAt,
        updatedAt = updatedAt,
        totalProgress = totalProgress,
        objectives = objectives.map(Objective::toResponse),
    )
}

