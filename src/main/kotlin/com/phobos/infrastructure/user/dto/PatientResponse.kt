package com.phobos.infrastructure.user.dto

import com.fasterxml.jackson.annotation.JsonFormat
import com.phobos.domain.user.Patient
import com.phobos.domain.user.UserType
import java.time.LocalDate

data class PatientResponse(
    override val userType: UserType = UserType.PATIENT,
    override val id: Int,
    override val name: String,
    override val email: String,
    override val profileImagePath: String?,
    override val releaseDate: LocalDate?,
    override val telephone: String?,
    override val address: String?,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    val birthdate: LocalDate?,
    val gender: String?
) : UserResponse()

fun Patient.toResponse(): PatientResponse {
    return PatientResponse(
        id = this.id,
        name = this.name,
        email = this.email,
        profileImagePath = this.profileImagePath,
        releaseDate = this.releaseDate,
        telephone = this.telephone,
        address = this.address,
        birthdate = this.birthdate,
        gender = this.gender
    )
}
