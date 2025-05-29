package com.phobos.infrastructure.mentaldisorder

import com.phobos.domain.mentaldisorder.MentalDisorder

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