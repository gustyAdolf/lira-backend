package com.lira.infrastructure.user.dto

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.lira.domain.user.Company
import com.lira.domain.user.Patient
import com.lira.domain.user.Therapist
import com.lira.domain.user.User
import com.lira.domain.user.UserType
import java.time.LocalDate


@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.EXISTING_PROPERTY,
    property = "userType",
    visible = true
)
@JsonSubTypes(
    JsonSubTypes.Type(value = PatientRequest::class, name = "PATIENT"),
    JsonSubTypes.Type(value = TherapistRequest::class, name = "THERAPIST"),
    JsonSubTypes.Type(value = CompanyRequest::class, name = "COMPANY")
)
sealed class UserRequest {
    abstract val id: Int
    abstract val companyId: Int
    abstract val userType: UserType
    abstract val name: String
    abstract val email: String
    abstract val password: String
    abstract val profileImagePath: String?
    abstract val telephone: String?
    abstract val address: String?
    abstract val releaseDate: LocalDate?
}

fun UserRequest.toDomain(userImagePath: String, encodePassword: String): User = when (this) {
    is PatientRequest -> Patient(
        companyId = companyId,
        name = name,
        email = email,
        password = encodePassword,
        telephone = telephone,
        address = address,
        profileImagePath = userImagePath,
        releaseDate = releaseDate,
        birthdate = birthdate,
        gender = gender
    )

    is TherapistRequest -> Therapist(
        companyId = companyId,
        name = name,
        email = email,
        password = encodePassword,
        telephone = telephone,
        address = address,
        profileImagePath = userImagePath,
        releaseDate = releaseDate,
        licenseNumber = licenseNumber
    )

    is CompanyRequest -> Company(
        companyId = companyId,
        name = name,
        email = email,
        password = encodePassword,
        telephone = telephone,
        address = address,
        profileImagePath = userImagePath,
        releaseDate = releaseDate,
        cif = cif,
        companyAddress = companyAddress
    )
}


