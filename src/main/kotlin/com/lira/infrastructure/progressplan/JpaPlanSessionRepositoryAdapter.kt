package com.lira.infrastructure.progressplan

import com.lira.domain.progressplan.PlanSession
import com.lira.domain.progressplan.PlanSessionRepository
import com.lira.infrastructure.progressplan.entity.toDomain
import com.lira.infrastructure.progressplan.entity.toEntity
import com.lira.infrastructure.progressplan.jpa.JpaPlanSessionRepository
import org.springframework.stereotype.Repository

@Repository
class JpaPlanSessionRepositoryAdapter(
    private val jpaPlanSessionRepository: JpaPlanSessionRepository
) : PlanSessionRepository {

    override fun save(session: PlanSession): PlanSession {
        return jpaPlanSessionRepository.save(session.toEntity()).toDomain()
    }

    override fun findByPlanId(planId: Int): List<PlanSession> {
        return jpaPlanSessionRepository.findByPlanIdOrderBySessionDateDesc(planId).map { it.toDomain() }
    }
}
