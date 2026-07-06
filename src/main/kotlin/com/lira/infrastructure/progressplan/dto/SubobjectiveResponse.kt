package com.lira.infrastructure.progressplan.dto

import com.lira.domain.progressplan.Subobjective
import com.lira.domain.progressplan.SubobjectiveType
import java.time.LocalDateTime

data class SubobjectiveResponse(
    val id: Int,
    val title: String,
    val description: String?,
    val type: SubobjectiveType,
    val targetValue: Int?,
    val targetSuccess: Int?,
    val targetFail: Int?,
    val currentProgress: Double,
    val currentValue: Int,
    val currentSuccess: Int,
    val currentFail: Int,
    val isCompleted: Boolean,
    val createdAt: LocalDateTime
)

fun Subobjective.toResponse(): SubobjectiveResponse {
    return SubobjectiveResponse(
        id = id,
        title = title,
        description = description,
        type = type,
        targetValue = targetValue,
        targetSuccess = targetSuccess,
        targetFail = targetFail,
        currentProgress = currentProgress,
        currentValue = currentValue,
        currentSuccess = currentSuccess,
        currentFail = currentFail,
        isCompleted = isCompleted,
        createdAt = createdAt
    )
}
