package com.lira.infrastructure.progressplan.jpa

import com.lira.infrastructure.progressplan.entity.SubobjectiveEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.transaction.annotation.Transactional

interface JpaSubobjectiveRepository : JpaRepository<SubobjectiveEntity, Int> {
    fun findByObjectiveId(objectiveId: Int): List<SubobjectiveEntity>

    @Modifying
    @Transactional
    @Query("DELETE FROM SubobjectiveEntity s WHERE s.id = :id")
    fun deleteSubobjective(@Param("id") id: Int)
}