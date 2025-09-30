package com.phobos.infrastructure.user.entity

import com.phobos.domain.user.Therapist
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table

@Entity
@Table(name = "therapists")
class TherapistEntity(

    @Column(name = "license_number")
    val licenseNumber: String?,

    ) : UserEntity()

fun TherapistEntity.toDomain(): Therapist = Therapist(
    id = this.id,
    name = this.name,
    email = this.email,
    password = this.password,
    profileImagePath = this.profileImagePath,
    telephone = this.telephone,
    address = this.address,
    releaseDate = releaseDate,
    licenseNumber = this.licenseNumber
)