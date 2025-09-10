package com.phobos.domain.user

import com.phobos.infrastructure.user.dto.CompanyResponse
import com.phobos.infrastructure.user.dto.PatientResponse
import com.phobos.infrastructure.user.dto.TherapistResponse
import com.phobos.infrastructure.user.dto.UserResponse
import java.time.LocalDate

sealed class User {
    abstract val id: Int
    abstract val userType: UserType
    abstract val name: String
    abstract val email: String
    abstract val password: String
    abstract val profileImagePath: String?
    abstract val telephone: String?
    abstract val address: String?
    abstract val releaseDate: LocalDate?
}

fun User.toResponse(): UserResponse = when (this) {
    is Patient -> PatientResponse(
        id = id,
        name = name,
        email = email,
        profileImagePath = profileImagePath,
        releaseDate = releaseDate,
        telephone = telephone,
        address = address,
        birthdate = birthdate,
        gender = gender,
    )

    is Therapist -> TherapistResponse(
        id = id,
        name = name,
        email = email,
        profileImagePath = profileImagePath,
        releaseDate = releaseDate,
        telephone = telephone,
        address = address,
        licenseNumber = licenseNumber
    )

    is Company -> CompanyResponse(
        id = id,
        name = name,
        email = email,
        profileImagePath = profileImagePath,
        releaseDate = releaseDate,
        telephone = telephone,
        address = address,
        companyAddress = companyAddress,
        cif = cif
    )
}