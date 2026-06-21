package com.lira.domain.progressplan

import com.lira.infrastructure.progressplan.entity.SubobjectiveEntity
import com.lira.infrastructure.progressplan.entity.SubobjectiveType
import java.time.LocalDateTime

data class Subobjective(
    var id: Int = 0,
    val title: String,
    val description: String?,
    val type: SubobjectiveType,
    val targetValue: Int?,
    val targetSuccess: Int?,
    val targetFail: Int?,
    var currentProgress: Double = 0.0,
    val createdAt: LocalDateTime = LocalDateTime.now()
)

fun Subobjective.toEntity(): SubobjectiveEntity =
    SubobjectiveEntity(
        id = this.id,
        title = title,
        description = description,
        type = type,
        targetValue = targetValue,
        targetSuccess = targetSuccess,
        targetFail = targetFail,
        currentProgress = currentProgress,
        createdAt = createdAt
    )