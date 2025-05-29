package com.phobos.application.session

import com.phobos.domain.session.Session
import com.phobos.domain.session.SessionRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class GetSessions(
    private val sessionRepository: SessionRepository
) {

    fun execute(userId: Int, mentalDisorderId: Int, pageable: Pageable): Page<Session> {

        return sessionRepository.getSessionByUserIdAndMentalDisorderId(userId, mentalDisorderId, pageable)
    }
}