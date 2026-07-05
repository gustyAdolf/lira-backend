package com.lira.infrastructure.patienttask.dto

import com.fasterxml.jackson.annotation.JsonFormat
import com.lira.domain.patienttask.PatientTaskEntry
import java.time.LocalDateTime

data class PatientTaskEntryResponse(
    val id: Int,
    val patientId: Int,
    val subobjectiveId: Int?,
    val patientTaskId: Int?,
    val note: String?,
    val emotionEntryId: Int?,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    val createdAt: LocalDateTime,
)

fun PatientTaskEntry.toResponse() = PatientTaskEntryResponse(
    id = id,
    patientId = patientId,
    subobjectiveId = subobjectiveId,
    patientTaskId = patientTaskId,
    note = note,
    emotionEntryId = emotionEntryId,
    createdAt = createdAt,
)
