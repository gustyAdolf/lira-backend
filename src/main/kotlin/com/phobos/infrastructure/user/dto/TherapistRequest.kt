package com.phobos.infrastructure.user.dto

import com.phobos.domain.user.Therapist
import com.phobos.domain.user.UserType
import java.time.LocalDate

data class TherapistRequest(
    override val userType: UserType = UserType.THERAPIST,
    override val name: String,
    override val email: String,
    override val password: String,
    override val address: String,
    override val id: Int,
    override val profileImagePath: String?,
    override val telephone: String?,
    override val releaseDate: LocalDate?,
    val licenseNumber: String
) : UserRequest()

fun TherapistRequest.toDomain(imagePath: String?, encodePassword: String): Therapist {
    return Therapist(
        name = name,
        email = email,
        password = encodePassword,
        profileImagePath = imagePath ?: "",
        telephone = telephone,
        address = address,
        releaseDate = releaseDate,
        licenseNumber = licenseNumber
    )
}


