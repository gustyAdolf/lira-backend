package com.lira.infrastructure.progressplan.jpa

import com.lira.infrastructure.progressplan.entity.ObjectiveEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.transaction.annotation.Transactional

interface JpaObjectiveRepository : JpaRepository<ObjectiveEntity, Int> {
    fun findByProgressPlanIdOrderByOrderIndex(progressPlanId: Int): List<ObjectiveEntity>

    @Modifying
    @Transactional
    @Query("DELETE FROM ObjectiveEntity o WHERE o.id = :id")
    fun deleteObjective(@Param("id") id: Int)
}