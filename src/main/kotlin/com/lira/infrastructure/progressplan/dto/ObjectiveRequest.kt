package com.lira.infrastructure.progressplan.dto

import com.lira.domain.progressplan.Objective

data class ObjectiveRequest(
    val title: String,
    val description: String?,
    val orderIndex: Int,
    val subobjectives: List<SubobjectiveRequest>
)

fun ObjectiveRequest.toDomain(): Objective {
    return Objective(
        title = title,
        description = description,
        orderIndex = orderIndex,
        subobjectives = subobjectives.map(SubobjectiveRequest::toDomain)
    )
}
