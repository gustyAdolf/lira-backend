package com.lira.infrastructure.mentaldisorder

import com.lira.domain.mentaldisorder.MentalDisorder

data class MentalDisorderResponse(
    val id: Int,
    val mentalDisorder: String
)

fun MentalDisorder.toResponse(): MentalDisorderResponse {
    return MentalDisorderResponse(
        id = id,
        mentalDisorder = name
    )
}