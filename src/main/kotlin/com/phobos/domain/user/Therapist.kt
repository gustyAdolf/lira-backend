package com.phobos.domain.user

import com.phobos.infrastructure.user.TherapistEntity
import com.phobos.infrastructure.user.entity.UserEntity
import java.time.LocalDate

data class Therapist(
    override val userType: UserType = UserType.THERAPIST,
    override val id: Int = 0,
    override val companyId: Int = 0,
    override val name: String = "",
    override val email: String = "",
    override val password: String = "",
    override val telephone: String? = null,
    override val address: String? = null,
    override val profileImagePath: String? = null,
    override val releaseDate: LocalDate? = null,
    val licenseNumber: String? = null
) : User()

fun Therapist.toEntity(): TherapistEntity {
    val user = UserEntity(
        name = name,
        email = email,
        password = password,
        profileImagePath = profileImagePath,
        telephone = telephone,
        address = address,
        userType = userType
    )
    return TherapistEntity(
        licenseNumber = licenseNumber,
        user = user
    )
}
