package com.lira.infrastructure.patienttask.dto

import com.fasterxml.jackson.annotation.JsonFormat
import com.lira.domain.patienttask.PatientTask
import java.time.LocalDateTime

data class PatientTaskResponse(
    val id: Int,
    val patientId: Int,
    val title: String,
    val description: String?,
    val targetValue: Int?,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    val createdAt: LocalDateTime,
)

fun PatientTask.toResponse() = PatientTaskResponse(
    id = id,
    patientId = patientId,
    title = title,
    description = description,
    targetValue = targetValue,
    createdAt = createdAt,
)
