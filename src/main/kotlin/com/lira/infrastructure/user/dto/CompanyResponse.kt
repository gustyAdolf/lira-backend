package com.lira.infrastructure.user.dto

import com.lira.domain.user.UserType
import java.time.LocalDate

data class CompanyResponse(
    override val userType: UserType = UserType.PATIENT,
    override val id: Int,
    override val name: String,
    override val email: String,
    override val profileImagePath: String?,
    override val releaseDate: LocalDate?,
    override val telephone: String?,
    override val address: String?,
    val cif: String?,
    val companyAddress: String?
) : UserResponse()

fun CompanyResponse.toResponse(): CompanyResponse = CompanyResponse(
    id = this.id,
    name = this.name,
    email = this.email,
    profileImagePath = this.profileImagePath,
    releaseDate = this.releaseDate,
    telephone = this.telephone,
    address = this.address,
    cif = this.cif,
    companyAddress = this.companyAddress
)
