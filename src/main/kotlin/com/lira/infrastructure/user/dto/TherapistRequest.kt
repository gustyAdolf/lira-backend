package com.lira.infrastructure.user.dto

import com.lira.domain.user.Therapist
import com.lira.domain.user.UserType
import java.time.LocalDate

data class TherapistRequest(
    override val userType: UserType = UserType.THERAPIST,
    override val name: String,
    override val email: String,
    override val password: String,
    override val address: String,
    override val id: Int,
    override val companyId: Int,
    override val profileImagePath: String?,
    override val telephone: String?,
    override val releaseDate: LocalDate?,
    val licenseNumber: String
) : UserRequest()

fun TherapistRequest.toDomain(imagePath: String?, encodePassword: String): Therapist {
    return Therapist(
        companyId = companyId,
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


