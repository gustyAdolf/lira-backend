package com.lira.application.auth

import com.lira.application.user.UserQueryService
import com.lira.infrastructure.auth.AuthenticationResponse
import com.lira.infrastructure.security.JwtUtil
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
class AuthenticateUser(
    private val authenticationManager: AuthenticationManager,
    private val jwtUtil: JwtUtil,
    private val userDetailsService: UserDetailsService,
    private val userQueryService: UserQueryService
) {
    fun execute(email: String, password: String): AuthenticationResponse {
        authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(email, password)
        )
        val userDetails = userDetailsService.loadUserByUsername(email)
        val token = jwtUtil.generateToken(userDetails)
        val userResponse = userQueryService.getUserByEmail(email)
        return AuthenticationResponse(
            token = token,
            user = userResponse
        )
    }
}