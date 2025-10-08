package com.lira.infrastructure.auth

data class AuthenticationRequest(
    val email: String,
    val password: String
)
