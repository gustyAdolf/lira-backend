package com.phobos.infrastructure.auth

data class AuthenticationRequest(
    val email: String,
    val password: String
)
