package com.lira.domain.progressplan

import com.lira.domain.user.Patient
import com.lira.domain.user.Therapist
import com.lira.infrastructure.progressplan.entity.ProgressPlanEntity
import com.lira.infrastructure.user.entity.PatientEntity
import com.lira.infrastructure.user.entity.TherapistEntity
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

fun ProgressPlan.toEntity(
    patientEntity: PatientEntity,
    therapistEntity: TherapistEntity
): ProgressPlanEntity {
    val planEntity = ProgressPlanEntity(
        id = this.id,
        patient = patientEntity,
        therapist = therapistEntity,
        title = title,
        totalProgress = totalProgress,
        description = description,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
    val objectivesEntity = objectives.map { domainObj ->
        val objEntity = domainObj.toEntity()
        objEntity.progressPlan = planEntity
        objEntity.subobjectives.forEach { subEntity ->
            subEntity.objective = objEntity
        }
        objEntity
    }
    planEntity.objectives.addAll(objectivesEntity)
    return planEntity
}

