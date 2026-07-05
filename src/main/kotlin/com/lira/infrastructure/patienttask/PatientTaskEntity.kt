package com.lira.infrastructure.patienttask

import com.lira.domain.patienttask.PatientTask
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "patient_task")
class PatientTaskEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int = 0,

    @Column(name = "patient_id")
    val patientId: Int,

    @Column(name = "title")
    val title: String,

    @Column(name = "description")
    val description: String? = null,

    @Column(name = "target_value")
    val targetValue: Int? = null,

    @Column(name = "created_at")
    val createdAt: LocalDateTime = LocalDateTime.now(),
)

fun PatientTaskEntity.toDomain() = PatientTask(
    id = id,
    patientId = patientId,
    title = title,
    description = description,
    targetValue = targetValue,
    createdAt = createdAt,
)

fun PatientTask.toEntity() = PatientTaskEntity(
    patientId = patientId,
    title = title,
    description = description,
    targetValue = targetValue,
    createdAt = createdAt,
)
