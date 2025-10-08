package com.lira.infrastructure.security

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import java.util.*

@Service
class JwtUtil(
    @Value("\${jwt.secret}")
    private val secret: String,

    @Value("\${jwt.expirationMs}")
    private val jwtExpirationMs: Long,


    private val liraUserDetailsService: LiraUserDetailsService
) {


    fun extractUsername(token: String): String {
        return extractClaim(token, Claims::getSubject)
    }

    fun extractExpiration(token: String): Date {
        return extractClaim(token, Claims::getExpiration)
    }

    fun <T> extractClaim(token: String, claimsResolver: (Claims) -> T): T {
        val claims = extractAllClaims(token)
        return claimsResolver(claims)
    }

    private fun extractAllClaims(token: String): Claims {
        return Jwts.parser()
            .setSigningKey(secret)
            .parseClaimsJws(token)
            .body
    }

    private fun isTokenExpired(token: String): Boolean {
        return extractExpiration(token).before(Date())
    }

    fun generateToken(userDetails: UserDetails, neverExpire: Boolean = false): String {
        val claims: Map<String, Any> = HashMap()
        return createToken(claims, userDetails.username, neverExpire)
    }

    fun generateRefreshToken(userDetails: UserDetails, neverExpire: Boolean = false): String {
        val claims: Map<String, Any> = HashMap()
        return createToken(claims, userDetails.username, neverExpire)
    }

    private fun createToken(claims: Map<String, Any>, subject: String, neverExpire: Boolean = false): String {
       val builder = Jwts.builder()
           .setClaims(claims)
           .setSubject(subject)
           .setIssuedAt(Date())
           .signWith(SignatureAlgorithm.HS256, secret)


        if (!neverExpire) {
            builder.setExpiration(Date(System.currentTimeMillis() + jwtExpirationMs))
        }

        return builder.compact()
    }

    fun validateToken(token: String?, userDetails: UserDetails): Boolean {
        return if (token == null) {
            false
        } else {
            val username = extractUsername(token)
            (username == userDetails.username)
        }
    }

    fun validateRefreshToken(token: String, userDetails: UserDetails): Boolean {
        return validateToken(token, userDetails) // Add any additional validation if needed
    }

    fun getUserDetailsFromRefreshToken(token: String): UserDetails {
        val username = extractUsername(token)
        return liraUserDetailsService.loadUserByUsername(username)
    }

}