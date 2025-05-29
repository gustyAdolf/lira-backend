package com.phobos.infrastructure.auth

import com.phobos.infrastructure.user.dto.UserResponse

data class AuthenticationResponse(
    val token: String,
    val user: UserResponse
)
