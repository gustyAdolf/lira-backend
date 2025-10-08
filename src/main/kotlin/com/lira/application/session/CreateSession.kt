package com.lira.application.session

import com.lira.domain.session.Session
import com.lira.domain.session.SessionRepository
import com.lira.infrastructure.session.SessionRequest
import org.springframework.stereotype.Service

@Service
class CreateSession(
    private var sessionRepository: SessionRepository
) {

    fun execute(sessionRequest: SessionRequest): Session {
        val session = Session(
            activationLevel = sessionRequest.activationLevel,
            exposureLevel = sessionRequest.exposureLevel,
            userId = sessionRequest.userId,
            mentalDisorderId = sessionRequest.mentalDisorderId,
            progress = sessionRequest.progress,
            therapistNotes = sessionRequest.therapistNotes,
            userNotes = sessionRequest.userNotes,
        )
        return sessionRepository.createSession(session)
    }
}