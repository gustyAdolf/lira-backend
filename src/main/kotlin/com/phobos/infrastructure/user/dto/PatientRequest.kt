package com.phobos.infrastructure.user.dto

import com.fasterxml.jackson.annotation.JsonFormat
import com.phobos.domain.user.Patient
import com.phobos.domain.user.UserType
import java.time.LocalDate

class PatientRequest(
    override val userType: UserType = UserType.PATIENT,
    override val name: String,
    override val email: String,
    override val password: String,
    override val address: String,
    override val releaseDate: LocalDate = LocalDate.now(),
    override val id: Int,
    override val profileImagePath: String?,
    override val telephone: String?,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    val birthdate: LocalDate,
    val gender: String,
) : UserRequest()

fun PatientRequest.toDomain(imagePath: String?, encodePassword: String): Patient {
    return Patient(
        name = name,
        email = email,
        password = encodePassword,
        profileImagePath = imagePath ?: "",
        telephone = telephone,
        birthdate = birthdate,
        address = address,
        gender = gender,
        releaseDate = releaseDate
    )
}