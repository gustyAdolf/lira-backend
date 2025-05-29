package com.phobos.domain.session

import com.phobos.infrastructure.session.SessionEntity
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