package com.lira.infrastructure.progressplan.dto

import com.lira.domain.progressplan.Subobjective
import com.lira.infrastructure.progressplan.entity.SubobjectiveType

data class SubobjectiveRequest(
    val title: String,
    val description: String?,
    val type: SubobjectiveType,
    val targetValue: Int?,
    val targetSuccess: Int?,
    val targetFail: Int?,
)

fun SubobjectiveRequest.toDomain(): Subobjective {
    return Subobjective(
        title = title,
        description = description,
        type = type,
        targetValue = targetValue,
        targetSuccess = targetSuccess,
        targetFail = targetFail
    )
}