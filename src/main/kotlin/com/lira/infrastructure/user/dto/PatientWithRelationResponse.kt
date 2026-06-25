package com.lira.infrastructure.user.dto

import com.fasterxml.jackson.annotation.JsonFormat
import com.lira.domain.user.PatientWithRelation
import com.lira.domain.user.UserType
import java.time.LocalDate

data class PatientWithRelationResponse(
    val userType: UserType = UserType.PATIENT,
    val id: Int,
    val name: String,
    val email: String,
    val profileImagePath: String?,
    val releaseDate: LocalDate?,
    val telephone: String?,
    val address: String?,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    val birthdate: LocalDate?,
    val gender: String?,
    val isMyPatient: Boolean
)

fun PatientWithRelation.toResponse() = PatientWithRelationResponse(
    id = patient.id,
    name = patient.name,
    email = patient.email,
    profileImagePath = patient.profileImagePath,
    releaseDate = patient.releaseDate,
    telephone = patient.telephone,
    address = patient.address,
    birthdate = patient.birthdate,
    gender = patient.gender,
    isMyPatient = isMyPatient
)
