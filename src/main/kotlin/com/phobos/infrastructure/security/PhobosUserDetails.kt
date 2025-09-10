package com.phobos.infrastructure.security

import com.phobos.domain.user.Patient
import com.phobos.domain.user.User
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails


class PhobosUserDetails(private val patient: User) : UserDetails {

    override fun getAuthorities(): List<SimpleGrantedAuthority> {
        return listOf(SimpleGrantedAuthority(patient.userType.name))
    }

    override fun getPassword(): String {
        return patient.password
    }

    override fun getUsername(): String {
        return patient.email
    }

    override fun isAccountNonExpired(): Boolean {
        return true
    }

    override fun isAccountNonLocked(): Boolean {
        return true
    }

    override fun isCredentialsNonExpired(): Boolean {
        return true
    }

    override fun isEnabled(): Boolean {
        return true
    }
}