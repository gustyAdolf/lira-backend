package com.lira.infrastructure.progressplan.dto

import com.lira.domain.progressplan.Objective
import com.lira.domain.progressplan.Subobjective
import java.time.LocalDateTime

data class ObjectiveResponse(
    val id: Int,
    val title: String,
    val description: String?,
    val orderIndex: Int,
    val createdAt: LocalDateTime,
    val subobjectives: List<SubobjectiveResponse>
)

fun Objective.toResponse(): ObjectiveResponse {
    return ObjectiveResponse(
        id = id,
        title = title,
        description = description,
        orderIndex = orderIndex,
        subobjectives = subobjectives.map(Subobjective::toResponse),
        createdAt = createdAt
    )
}
