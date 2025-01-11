package com.phobos.application.repository

import com.phobos.infrastructure.persistence.user.User
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<User, Int> {

    fun findByEmail(email: String): User?

    fun findByNameContainingIgnoreCase(name: String, pageable: Pageable): Page<User>

    fun findByUserDisordersMentalDisorderMentalDisorderContainingIgnoreCase(disorder: String, pageable: Pageable): Page<User>
}