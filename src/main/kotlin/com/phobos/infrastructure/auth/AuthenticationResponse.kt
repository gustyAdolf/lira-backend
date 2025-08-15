package com.phobos.infrastructure.auth

import com.phobos.domain.user.User

data class AuthenticationResponse(
    val token: String,
    val user: User
)
