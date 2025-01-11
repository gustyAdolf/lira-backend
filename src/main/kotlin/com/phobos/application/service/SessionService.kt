package com.phobos.application.service

import com.phobos.application.dto.session.SessionRequest
import com.phobos.application.dto.session.SessionResponse
import com.phobos.application.repository.SessionRepository
import com.phobos.infrastructure.mapper.SessionMapper
import com.phobos.infrastructure.persistence.Session
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class SessionService(
    @Autowired private val sessionRepository: SessionRepository
) {

    @Transactional(readOnly = true)
    fun getSessions(userId: Int, mentalDisorderId: Int, pageable: Pageable): Page<SessionResponse> {
        return sessionRepository.findSessionByUserIdAndMentalDisorderId(userId, mentalDisorderId, pageable)
            .map { SessionMapper.entityToResponse(it) }
    }

    @Transactional
    fun addSession(session: SessionRequest) {
        val newSession: Session = SessionMapper.requestToEntity(session)
        sessionRepository.save(newSession)
    }
}