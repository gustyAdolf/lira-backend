package com.lira.infrastructure.progressplan.jpa

import com.lira.infrastructure.progressplan.entity.ObjectiveEntity
import org.springframework.data.jpa.repository.JpaRepository

interface JpaObjectiveRepository : JpaRepository<ObjectiveEntity, Int> {
    fun findByProgressPlanIdOrderByOrderIndex(progressPlanId: Int): List<ObjectiveEntity>
}