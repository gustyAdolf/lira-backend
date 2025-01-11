package com.phobos.application.repository

import com.phobos.infrastructure.persistence.Session
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository

interface SessionRepository : JpaRepository<Session, Int> {
    fun findSessionByUserIdAndMentalDisorderId(userId: Int, mentalDisorderId: Int, pageable: Pageable): Page<Session>
}