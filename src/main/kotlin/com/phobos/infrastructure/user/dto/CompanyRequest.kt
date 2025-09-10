package com.phobos.infrastructure.user.dto

import com.phobos.domain.user.UserType
import java.time.LocalDate

data class CompanyRequest(
    override val userType: UserType = UserType.COMPANY,
    override val name: String,
    override val email: String,
    override val password: String,
    override val address: String,
    override val releaseDate: LocalDate = LocalDate.now(),
    override val id: Int,
    override val companyId: Int = 0,
    override val profileImagePath: String?,
    override val telephone: String?,
    val companyAddress: String?,
    val cif: String?,
) : UserRequest()
