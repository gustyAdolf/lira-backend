package com.lira.infrastructure.mentaldisorder

import com.lira.domain.mentaldisorder.MentalDisorder

data class MentalDisorderResponse(
    val id: Int,
    val name: String
)

fun MentalDisorder.toResponse(): MentalDisorderResponse {
    return MentalDisorderResponse(
        id = id,
        name = name
    )
}