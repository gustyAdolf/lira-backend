package com.lira.domain.patienttask

import java.time.LocalDateTime

data class PatientTask(
    val id: Int = 0,
    val patientId: Int,
    val title: String,
    val description: String? = null,
    val targetValue: Int? = null,
    val createdAt: LocalDateTime = LocalDateTime.now(),
)
