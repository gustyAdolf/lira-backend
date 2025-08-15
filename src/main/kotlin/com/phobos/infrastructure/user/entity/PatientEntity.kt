package com.phobos.infrastructure.user.entity

import com.phobos.domain.user.Patient
import com.phobos.domain.user.UserType
import com.phobos.infrastructure.user.UserDisorderEntity
import jakarta.persistence.*
import java.time.LocalDate

@Entity
@Table(name = "patients")
class PatientEntity (
    @Id
    val id: Int = 0,

    @Column(name = "birthdate")
    val birthdate: LocalDate? = null,

    @Column(name = "gender")
    val gender: String? = null,

    @OneToOne
    @MapsId
    @JoinColumn(name = "id")
    val user: UserEntity,

    @OneToMany(mappedBy = "patient", fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    val userDisorders: List<UserDisorderEntity> = emptyList()
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

fun PatientEntity.toDomain(): Patient = Patient(
    id = this.id,
    name = this.name,
    email = this.email,
    password = this.password,
    profileImagePath = this.profileImagePath,
    telephone = this.telephone,
    address = this.address,
    releaseDate = releaseDate,
    birthdate = birthdate,
    gender = gender,
)