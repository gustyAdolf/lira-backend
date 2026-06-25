package com.lira.infrastructure.progressplan.dto

import com.lira.domain.progressplan.ProgressPlan
import com.lira.domain.user.Patient
import com.lira.domain.user.Therapist

data class ProgressPlanRequest(
    val patientId: Int,
    val therapistId: Int,
    val title: String,
    val description: String?,
    val mentalDisorderId: Int? = null,
    val objectives: List<ObjectiveRequest>,
)

fun ProgressPlanRequest.toDomain(): ProgressPlan {
    val patient = Patient(id = patientId)
    val therapist = Therapist(id = therapistId)
    return ProgressPlan(
        patient = patient,
        therapist = therapist,
        title = title,
        description = description,
        mentalDisorderId = mentalDisorderId,
        objectives = objectives.map(ObjectiveRequest::toDomain)
    )
}
