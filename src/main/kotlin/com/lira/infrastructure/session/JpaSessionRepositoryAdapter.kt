package com.lira.infrastructure.session

import com.lira.domain.session.Session
import com.lira.domain.session.SessionRepository
import com.lira.infrastructure.session.toEntity
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository

@Repository
class JpaSessionRepositoryAdapter(
    private val jpaSessionRepository: JpaSessionRepository
) : SessionRepository {

    override fun getSessionByUserIdAndMentalDisorderId(
        userId: Int,
        mentalDisorderId: Int,
        pageable: Pageable
    ): Page<Session> {

        return jpaSessionRepository.findSessionByUserIdAndMentalDisorderId(
            userId, mentalDisorderId, pageable
        ).map { it.toDomain() }
    }

    override fun createSession(session: Session): Session {
        val entity = session.toEntity()
        return jpaSessionRepository.save(entity).toDomain()
    }

}