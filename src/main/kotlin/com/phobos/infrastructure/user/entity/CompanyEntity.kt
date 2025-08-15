package com.phobos.infrastructure.user.entity

import com.phobos.domain.user.Company
import com.phobos.domain.user.UserType
import jakarta.persistence.*
import java.time.LocalDate

@Entity
@Table(name = "companies")
class CompanyEntity(
    @Id
    val id: Int = 0,

    @Column(name = "cif")
    val cif: String?,

    @Column(name = "company_address")
    val companyAddress: String? = null,

    @OneToOne
    @MapsId
    @JoinColumn(name = "id")
    val user: UserEntity
) {
    val name: String get() = user.name
    val email: String get() = user.email
    val password: String get() = user.password
    val profileImagePath: String? get() = user.profileImagePath
    val telephone: String? get() = user.telephone
    val address: String? get() = user.address
    val userType: UserType get() = user.userType
    val releaseDate: LocalDate get() = user.releaseDate
}

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
)