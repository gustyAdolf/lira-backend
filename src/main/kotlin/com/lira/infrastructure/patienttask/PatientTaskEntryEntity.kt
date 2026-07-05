package com.lira.infrastructure.patienttask

import com.lira.domain.patienttask.PatientTaskEntry
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "patient_task_entry")
class PatientTaskEntryEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int = 0,

    @Column(name = "patient_id")
    val patientId: Int,

    @Column(name = "subobjective_id")
    val subobjectiveId: Int? = null,

    @Column(name = "patient_task_id")
    val patientTaskId: Int? = null,

    @Column(name = "note")
    val note: String? = null,

    @Column(name = "emotion_entry_id")
    val emotionEntryId: Int? = null,

    @Column(name = "created_at")
    val createdAt: LocalDateTime = LocalDateTime.now(),
)

fun PatientTaskEntryEntity.toDomain() = PatientTaskEntry(
    id = id,
    patientId = patientId,
    subobjectiveId = subobjectiveId,
    patientTaskId = patientTaskId,
    note = note,
    emotionEntryId = emotionEntryId,
    createdAt = createdAt,
)

fun PatientTaskEntry.toEntity() = PatientTaskEntryEntity(
    patientId = patientId,
    subobjectiveId = subobjectiveId,
    patientTaskId = patientTaskId,
    note = note,
    emotionEntryId = emotionEntryId,
    createdAt = createdAt,
)
