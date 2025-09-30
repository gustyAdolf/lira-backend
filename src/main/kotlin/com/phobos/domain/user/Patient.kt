package com.phobos.domain.user

import com.phobos.domain.mentaldisorder.MentalDisorder
import com.phobos.infrastructure.user.entity.PatientEntity
import java.time.LocalDate

data class Patient(
    override val userType: UserType = UserType.PATIENT,
    override val id: Int = 0,
    override val companyId: Int = 0,
    override val name: String = "",
    override val email: String = "",
    override val password: String = "",
    override val telephone: String? = null,
    override val address: String? = null,
    override val profileImagePath: String? = null,
    override val releaseDate: LocalDate? = null,
    val birthdate: LocalDate? = null,
    val gender: String? = null,
    val mentalDisorders: List<MentalDisorder> = emptyList()
) : User()

fun Patient.toEntity(): PatientEntity {
    return PatientEntity(
        birthdate = birthdate,
        gender = gender
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
