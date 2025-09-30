package com.phobos.domain.user

import com.phobos.infrastructure.user.entity.CompanyEntity
import com.phobos.infrastructure.user.entity.UserEntity
import java.time.LocalDate

data class Company(
    override val userType: UserType = UserType.COMPANY,
    override val id: Int = 0,
    override val companyId: Int = 0,
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
    return CompanyEntity(
        companyAddress = companyAddress,
        cif = cif
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
