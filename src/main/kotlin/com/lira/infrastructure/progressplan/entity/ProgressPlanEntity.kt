package com.lira.infrastructure.progressplan.entity

import com.lira.domain.progressplan.Objective
import com.lira.domain.progressplan.ProgressPlan
import com.lira.infrastructure.user.entity.PatientEntity
import com.lira.infrastructure.user.entity.TherapistEntity
import com.lira.infrastructure.user.entity.toDomain
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "progress_plan")
data class ProgressPlanEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int = 0,

    @ManyToOne
    @JoinColumn(name = "patient_id")
    val patient: PatientEntity,

    @ManyToOne
    @JoinColumn(name = "therapist_id")
    val therapist: TherapistEntity,

    @Column(name = "title")
    val title: String,

    @Column(name = "description")
    val description: String?,

    @Column(name = "total_progress")
    val totalProgress: Double,

    @Column(name = "created_at")
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @Column(name = "updated_at")
    val updatedAt: LocalDateTime = LocalDateTime.now(),

    @OneToMany(mappedBy = "progressPlan", cascade = [CascadeType.ALL], orphanRemoval = true)
    val objectives: MutableList<ObjectiveEntity> = mutableListOf()
)

fun ProgressPlanEntity.toDomain(): ProgressPlan =
    ProgressPlan(
        id = this.id,
        patient = this.patient.toDomain(),
        therapist = this.therapist.toDomain(),
        title = this.title,
        description = this.description,
        totalProgress = this.totalProgress,
        objectives = this.objectives.map(ObjectiveEntity::toDomain),
        createdAt = this.createdAt,
        updatedAt = this.updatedAt
    )

fun ProgressPlan.toEntity(
    patientEntity: PatientEntity,
    therapistEntity: TherapistEntity
): ProgressPlanEntity {
    val planEntity = ProgressPlanEntity(
        id = id,
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
        objEntity.subobjectives.forEach { subEntity -> subEntity.objective = objEntity }
        objEntity
    }
    planEntity.objectives.addAll(objectivesEntity)
    return planEntity
}

fun Objective.toEntity(): ObjectiveEntity =
    ObjectiveEntity(
        id = id,
        title = title,
        description = description,
        orderIndex = orderIndex,
        createdAt = createdAt,
        subobjectives = subobjectives.map { it.toEntity() }.toMutableList()
    )


