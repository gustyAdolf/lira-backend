package com.lira.domain.progressplan

import com.lira.infrastructure.progressplan.entity.ObjectiveEntity
import java.time.LocalDateTime

data class Objective(
    val id: Int = 0,
    val title: String,
    val description: String?,
    val orderIndex: Int,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val subobjectives: List<Subobjective>
)

fun Objective.toEntity(): ObjectiveEntity =
    ObjectiveEntity(
        id = id,
        title = title,
        description = description,
        orderIndex = orderIndex,
        createdAt = createdAt,
        subobjectives = subobjectives.map(Subobjective::toEntity).toMutableList()
    )
