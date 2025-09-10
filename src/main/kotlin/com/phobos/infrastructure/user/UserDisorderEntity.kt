package com.phobos.infrastructure.user

import com.phobos.infrastructure.mentaldisorder.MentalDisorderEntity
import com.phobos.infrastructure.user.entity.PatientEntity
import com.phobos.infrastructure.user.entity.UserEntity
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