package com.lira.infrastructure.user.dto

import com.lira.domain.user.Company
import com.lira.domain.user.Patient
import com.lira.domain.user.Therapist
import com.lira.domain.user.User

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
        cif = cif,
        companyAddress = companyAddress,
        cancellationWindowHours = cancellationWindowHours
    )
}
