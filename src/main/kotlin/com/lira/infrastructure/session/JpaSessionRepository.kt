package com.lira.infrastructure.session

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository

interface JpaSessionRepository : JpaRepository<SessionEntity, Int> {

    fun findSessionByUserIdAndMentalDisorderId(
        userId: Int,
        mentalDisorderId: Int,
        pageable: Pageable
    ): Page<SessionEntity>
}