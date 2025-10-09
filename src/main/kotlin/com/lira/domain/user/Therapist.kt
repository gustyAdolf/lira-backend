package com.lira.domain.user

import com.lira.infrastructure.user.entity.TherapistEntity
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
    return TherapistEntity(
        licenseNumber = licenseNumber
    ).apply {
        this.name = this@toEntity.name
        this.email = this@toEntity.email
        this.password = this@toEntity.password
        this.profileImagePath = this@toEntity.profileImagePath
        this.telephone = this@toEntity.telephone
        this.address = this@toEntity.address
        this.userType = this@toEntity.userType
    }
}
