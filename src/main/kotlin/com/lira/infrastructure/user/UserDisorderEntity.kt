package com.lira.infrastructure.user

import com.lira.infrastructure.mentaldisorder.MentalDisorderEntity
import com.lira.infrastructure.user.entity.PatientEntity
import jakarta.persistence.*

@Entity
@Table(name = "user_disorder")
data class UserDisorderEntity(
    @EmbeddedId
    val id: UserDisorderId,

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    val patient: PatientEntity,

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("mentalDisorderId")
    @JoinColumn(name = "mental_disorder_id")
    val mentalDisorder: MentalDisorderEntity
)

@Embeddable
data class UserDisorderId(
    val userId: Int,
    val mentalDisorderId: Int
)