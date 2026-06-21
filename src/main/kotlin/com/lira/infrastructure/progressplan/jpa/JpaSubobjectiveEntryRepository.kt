package com.lira.infrastructure.progressplan.jpa

import com.lira.infrastructure.progressplan.entity.SubobjectiveEntry
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface JpaSubobjectiveEntryRepository : JpaRepository<SubobjectiveEntry, Int> {

    @Query("SELECT COALESCE(SUM(e.valueIncrement), 0) FROM SubobjectiveEntry e WHERE e.subobjectiveId = :subId")
    fun sumValueBySubobjective(@Param("subId") subId: Int): Int

    @Query("SELECT COUNT(e) FROM SubobjectiveEntry e WHERE e.subobjectiveId= :subId AND e.isSuccess = true")
    fun countSuccessesBySubobjective(@Param("subId") subId: Int): Int
}