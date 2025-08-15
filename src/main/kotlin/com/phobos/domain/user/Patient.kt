package com.phobos.domain.user

import com.phobos.domain.mentaldisorder.MentalDisorder
import com.phobos.infrastructure.user.entity.PatientEntity
import com.phobos.infrastructure.user.entity.UserEntity
import java.time.LocalDate

data class Patient(
    override val userType: UserType = UserType.PATIENT,
    override val id: Int = 0,
    override val name: String,
    override val email: String,
    override val password: String,
    override val telephone: String? = null,
    override val address: String? = null,
    override val profileImagePath: String?,
    override val releaseDate: LocalDate?,
    val birthdate: LocalDate?,
    val gender: String?,
    val mentalDisorders: List<MentalDisorder> = emptyList()
) : User()

fun Patient.toEntity(): PatientEntity {
    val user = UserEntity(
        name = name,
        email = email,
        password = password,
        profileImagePath = profileImagePath,
        telephone = telephone,
        address = address,
        userType = UserType.PATIENT
    )
    return PatientEntity(
        birthdate = birthdate,
        gender = gender,
        user = user
    )
}
