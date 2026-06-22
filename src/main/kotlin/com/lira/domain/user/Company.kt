package com.lira.domain.user

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
