package com.lira.domain.user

import com.lira.domain.mentaldisorder.MentalDisorder
import java.time.LocalDate

data class Patient(
    override val userType: UserType = UserType.PATIENT,
    override val id: Int = 0,
    override val companyId: Int = 0,
    override val name: String = "",
    override val email: String = "",
    override val password: String = "",
    override val telephone: String? = null,
    override val address: String? = null,
    override val profileImagePath: String? = null,
    override val releaseDate: LocalDate? = null,
    val birthdate: LocalDate? = null,
    val gender: String? = null,
    val mentalDisorders: List<MentalDisorder> = emptyList()
) : User()
