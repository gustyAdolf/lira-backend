package com.phobos.infrastructure.user

import com.phobos.domain.user.Therapist
import com.phobos.domain.user.UserType
import com.phobos.infrastructure.user.entity.UserEntity
import jakarta.persistence.*
import java.time.LocalDate

@Entity
@Table(name = "therapists")
class TherapistEntity(
    @Id
    val id: Int = 0,

    @Column(name = "license_number")
    val licenseNumber: String?,

    @OneToOne
    @MapsId
    @JoinColumn(name = "id")
    val user: UserEntity
) {
    val name: String get() = user.name
    val email: String get() = user.email
    val password: String get() = user.password
    val profileImagePath: String? get() = user.profileImagePath
    val telephone: String? get() = user.telephone
    val address: String? get() = user.address
    val userType: UserType get() = user.userType
    val releaseDate: LocalDate get() = user.releaseDate
}

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