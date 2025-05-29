package com.phobos.infrastructure.mentaldisorder

import com.phobos.domain.mentaldisorder.MentalDisorder
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "mental_disorder")
data class MentalDisorderEntity(
    @Id
    val id: Int,

    @Column(name = "mental_disorder")
    val name: String
)

fun MentalDisorderEntity.toDomain(): MentalDisorder = MentalDisorder(
    id = this.id,
    name = this.name
)