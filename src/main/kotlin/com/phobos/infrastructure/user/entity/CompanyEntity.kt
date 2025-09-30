package com.phobos.infrastructure.user.entity

import com.phobos.domain.user.Company
import com.phobos.domain.user.UserType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table

@Entity
@Table(name = "companies")
class CompanyEntity(

    @Column(name = "cif")
    val cif: String?,

    @Column(name = "company_address")
    val companyAddress: String? = null
) : UserEntity()

fun CompanyEntity.toDomain(): Company = Company(
    id = this.id,
    name = this.name,
    email = this.email,
    password = this.password,
    profileImagePath = this.profileImagePath,
    telephone = this.telephone,
    address = this.address,
    releaseDate = this.releaseDate,
    cif = this.cif,
    companyAddress = this.companyAddress,
    userType = UserType.COMPANY,
    companyId = 0,
)