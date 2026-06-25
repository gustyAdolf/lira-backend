package com.lira.infrastructure.progressplan.jpa

import com.lira.infrastructure.progressplan.entity.PlanSessionEntity
import org.springframework.data.jpa.repository.JpaRepository

interface JpaPlanSessionRepository : JpaRepository<PlanSessionEntity, Int> {
    fun findByPlanIdOrderBySessionDateDesc(planId: Int): List<PlanSessionEntity>
}
