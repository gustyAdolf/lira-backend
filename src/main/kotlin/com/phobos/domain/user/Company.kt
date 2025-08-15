package com.phobos.domain.user

import com.phobos.infrastructure.user.entity.CompanyEntity
import com.phobos.infrastructure.user.entity.UserEntity
import java.time.LocalDate

data class Company(
    override val userType: UserType = UserType.COMPANY,
    override val id: Int = 0,
    override val name: String,
    override val email: String,
    override val password: String,
    override val telephone: String? = null,
    override val address: String? = null,
    override val profileImagePath: String?,
    override val releaseDate: LocalDate?,
    val cif: String?,
    val companyAddress: String?
) : User()

fun Company.toEntity(): CompanyEntity {
    val user = UserEntity(
        name = name,
        email = email,
        password = password,
        profileImagePath = profileImagePath,
        telephone = telephone,
        address = address,
        userType = userType
    )
    return CompanyEntity(
        companyAddress = companyAddress,
        cif = cif,
        user = user,
    )
}
