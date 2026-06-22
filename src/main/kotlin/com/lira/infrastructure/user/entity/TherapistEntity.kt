package com.lira.infrastructure.user.entity

import com.lira.domain.user.Therapist
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

fun Therapist.toEntity(): TherapistEntity = TherapistEntity(
    licenseNumber = licenseNumber
).apply {
    this.name = this@toEntity.name
    this.email = this@toEntity.email
    this.password = this@toEntity.password
    this.profileImagePath = this@toEntity.profileImagePath
    this.telephone = this@toEntity.telephone
    this.address = this@toEntity.address
    this.userType = this@toEntity.userType
}