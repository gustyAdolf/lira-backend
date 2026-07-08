package com.lira.infrastructure.auth

import com.lira.domain.user.ClinicSummary
import com.lira.domain.user.User

data class AuthenticationResponse(
    val token: String,
    val user: User,
    val clinics: List<ClinicSummary> = emptyList(),
)
