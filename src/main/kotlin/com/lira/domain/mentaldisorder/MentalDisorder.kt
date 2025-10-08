package com.lira.domain.mentaldisorder

import com.lira.infrastructure.mentaldisorder.MentalDisorderEntity

data class MentalDisorder(
    val id: Int,
    val name: String = "",
)


fun MentalDisorder.toEntity(): MentalDisorderEntity {
    return MentalDisorderEntity(id, name)
}