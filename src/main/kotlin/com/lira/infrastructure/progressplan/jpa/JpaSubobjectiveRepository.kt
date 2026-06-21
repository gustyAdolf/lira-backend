package com.lira.infrastructure.progressplan.jpa

import com.lira.infrastructure.progressplan.entity.SubobjectiveEntity
import org.springframework.data.jpa.repository.JpaRepository

interface JpaSubobjectiveRepository : JpaRepository<SubobjectiveEntity, Int> {
    fun findByObjectiveId(objectiveId: Int): List<SubobjectiveEntity>
}