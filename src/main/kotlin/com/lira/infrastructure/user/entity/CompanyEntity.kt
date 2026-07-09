package com.lira.infrastructure.user.entity

import com.lira.domain.user.Company
import com.lira.domain.user.UserType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table

@Entity
@Table(name = "companies")
class CompanyEntity(

    @Column(name = "cif")
    var cif: String?,

    @Column(name = "company_address")
    var companyAddress: String? = null,

    @Column(name = "cancellation_window_hours")
    var cancellationWindowHours: Int = 24
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
    cancellationWindowHours = this.cancellationWindowHours,
)

fun Company.toEntity(): CompanyEntity = CompanyEntity(
    cif = cif,
    companyAddress = companyAddress,
    cancellationWindowHours = cancellationWindowHours
).apply {
    this.name = this@toEntity.name
    this.email = this@toEntity.email
    this.password = this@toEntity.password
    this.profileImagePath = this@toEntity.profileImagePath
    this.telephone = this@toEntity.telephone
    this.address = this@toEntity.address
    this.userType = this@toEntity.userType
}