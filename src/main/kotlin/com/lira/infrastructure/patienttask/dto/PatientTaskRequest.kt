package com.lira.infrastructure.patienttask.dto

import com.lira.domain.patienttask.PatientTask

data class PatientTaskRequest(
    val patientId: Int,
    val title: String,
    val description: String? = null,
    val targetValue: Int? = null,
)

fun PatientTaskRequest.toDomain() = PatientTask(
    patientId = patientId,
    title = title,
    description = description,
    targetValue = targetValue,
)
