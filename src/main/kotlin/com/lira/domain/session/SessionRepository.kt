package com.lira.domain.session

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface SessionRepository {

    fun getSessionByUserIdAndMentalDisorderId(
        userId: Int,
        mentalDisorderId: Int,
        pageable: Pageable
    ): Page<Session>

    fun createSession(session: Session): Session
}