package com.phobos.infrastructure.user.dto

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.phobos.domain.user.Patient
import com.phobos.domain.user.Therapist
import com.phobos.domain.user.User
import com.phobos.domain.user.UserType
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
    abstract val userType: UserType
    abstract val name: String
    abstract val email: String
    abstract val password: String
    abstract val profileImagePath: String?
    abstract val telephone: String?
    abstract val address: String?
    abstract val releaseDate: LocalDate?
}

fun UserRequest.toDomain(): User = when (this) {
    is PatientRequest -> Patient(
        name = name,
        email = email,
        password = password,
        telephone = telephone,
        address = address,
        profileImagePath = profileImagePath,
        releaseDate = releaseDate,
        birthdate = birthdate,
        gender = gender
    )

    is TherapistRequest -> Therapist(
        name = name,
        email = email,
        password = password,
        telephone = telephone,
        address = address,
        profileImagePath = profileImagePath,
        releaseDate = releaseDate,
        licenseNumber = licenseNumber
    )

    is CompanyRequest -> TODO()
}


