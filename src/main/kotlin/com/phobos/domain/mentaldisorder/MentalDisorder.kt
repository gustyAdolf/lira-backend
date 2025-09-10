package com.phobos.domain.mentaldisorder

import com.phobos.infrastructure.mentaldisorder.MentalDisorderEntity

data class MentalDisorder(
    val id: Int,
    val name: String = "",
)


fun MentalDisorder.toEntity(): MentalDisorderEntity {
    return MentalDisorderEntity(id, name)
}