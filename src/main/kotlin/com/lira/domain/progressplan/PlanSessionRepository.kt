package com.lira.domain.progressplan

interface PlanSessionRepository {
    fun save(session: PlanSession): PlanSession
    fun findById(id: Int): PlanSession?
    fun findByPlanId(planId: Int): List<PlanSession>
}
