package com.phobos.infrastructure.user.dto

import com.phobos.domain.user.Therapist
import com.phobos.domain.user.UserType
import java.time.LocalDate

data class TherapistResponse(
    override val userType: UserType = UserType.THERAPIST,
    override val id: Int,
    override val name: String,
    override val email: String,
    override val profileImagePath: String?,
    override val releaseDate: LocalDate?,
    override val telephone: String?,
    override val address: String?,
    val licenseNumber: String?
) : UserResponse()

fun Therapist.toResponse(): TherapistResponse {
    return TherapistResponse(
        id = this.id,
        name = this.name,
        email = this.email,
        profileImagePath = this.profileImagePath,
        releaseDate = this.releaseDate,
        telephone = this.telephone,
        address = this.address,
        licenseNumber = this.licenseNumber
    )
}