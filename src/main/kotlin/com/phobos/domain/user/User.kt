package com.phobos.domain.user

import com.phobos.domain.mentaldisorder.MentalDisorder
import java.time.LocalDate

data class User(
    val id: Int,
    val name: String,
    val email: String,
    val password: String,
    val imageUrl: String,
    val userType: UserType,
    val birthdate: LocalDate? = null,
    val telephone: String? = null,
    val address: String? = null,
    val mentalDisorders: List<MentalDisorder> = emptyList()
)