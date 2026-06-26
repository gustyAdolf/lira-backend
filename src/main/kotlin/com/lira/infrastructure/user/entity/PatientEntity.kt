package com.lira.infrastructure.user.entity

import com.lira.domain.user.Patient
import com.lira.infrastructure.user.UserDisorderEntity
import jakarta.persistence.*
import java.time.LocalDate

@Entity
@Table(name = "patients")
class PatientEntity(

    @Column(name = "birthdate")
    val birthdate: LocalDate? = null,

    @Column(name = "gender")
    var gender: String? = null,

    @OneToMany(mappedBy = "patient", fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    val userDisorders: List<UserDisorderEntity> = emptyList()
) : UserEntity()

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

fun Patient.toEntity(): PatientEntity = PatientEntity(
    birthdate = birthdate,
    gender = gender
).apply {
    this.name = this@toEntity.name
    this.email = this@toEntity.email
    this.password = this@toEntity.password
    this.profileImagePath = this@toEntity.profileImagePath
    this.telephone = this@toEntity.telephone
    this.address = this@toEntity.address
    this.userType = this@toEntity.userType
}