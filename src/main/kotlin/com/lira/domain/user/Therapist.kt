package com.lira.domain.user

import java.time.LocalDate

data class Therapist(
    override val userType: UserType = UserType.THERAPIST,
    override val id: Int = 0,
    override val companyId: Int = 0,
    override val name: String = "",
    override val email: String = "",
    override val password: String = "",
    override val telephone: String? = null,
    override val address: String? = null,
    override val profileImagePath: String? = null,
    override val releaseDate: LocalDate? = null,
    val licenseNumber: String? = null
) : User()
