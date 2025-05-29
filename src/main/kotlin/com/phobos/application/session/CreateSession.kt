package com.phobos.application.session

import com.phobos.domain.session.Session
import com.phobos.domain.session.SessionRepository
import com.phobos.infrastructure.session.SessionRequest
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