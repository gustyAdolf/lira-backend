package com.phobos.infrastructure.user.entity

import com.phobos.domain.user.Patient
import com.phobos.infrastructure.user.UserDisorderEntity
import jakarta.persistence.*
import java.time.LocalDate

@Entity
@Table(name = "patients")
class PatientEntity(

    @Column(name = "birthdate")
    val birthdate: LocalDate? = null,

    @Column(name = "gender")
    val gender: String? = null,

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