package com.lira.infrastructure.auth

import com.lira.application.auth.AuthenticateUser
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/auth")
class AuthController(
    private val authenticateUser: AuthenticateUser
) {

    @PostMapping("/login")
    fun login(@RequestBody request: AuthenticationRequest): ResponseEntity<AuthenticationResponse> {

        val response = authenticateUser.execute(request.email, request.password)
        return ResponseEntity.ok(response)
    }
}