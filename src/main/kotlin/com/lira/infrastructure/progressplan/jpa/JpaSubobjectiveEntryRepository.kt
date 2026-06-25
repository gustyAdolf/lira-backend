package com.lira.infrastructure.progressplan.jpa

import com.lira.infrastructure.progressplan.entity.SubobjectiveEntryEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.transaction.annotation.Transactional

interface JpaSubobjectiveEntryRepository : JpaRepository<SubobjectiveEntryEntity, Int> {

    @Query("SELECT COALESCE(SUM(e.valueIncrement), 0) FROM SubobjectiveEntryEntity e WHERE e.subobjectiveId = :subId")
    fun sumValueBySubobjective(@Param("subId") subId: Int): Int

    @Query("SELECT COUNT(e) FROM SubobjectiveEntryEntity e WHERE e.subobjectiveId= :subId AND e.isSuccess = true")
    fun countSuccessesBySubobjective(@Param("subId") subId: Int): Int

    @Query("SELECT COUNT(e) FROM SubobjectiveEntryEntity e WHERE e.subobjectiveId = :subId AND e.isSuccess = false")
    fun countFailsBySubobjective(@Param("subId") subId: Int): Int

    @Query("""
        SELECT e.subobjectiveId,
               COALESCE(SUM(e.valueIncrement), 0),
               SUM(CASE WHEN e.isSuccess = true THEN 1 ELSE 0 END),
               SUM(CASE WHEN e.isSuccess = false THEN 1 ELSE 0 END)
        FROM SubobjectiveEntryEntity e
        WHERE e.subobjectiveId IN :subIds
        GROUP BY e.subobjectiveId
    """)
    fun aggregateBySubobjectiveIds(@Param("subIds") subIds: Collection<Int>): List<Array<Any>>

    fun findTop3BySubobjectiveIdOrderByEntryDateDesc(subobjectiveId: Int): List<SubobjectiveEntryEntity>

    @Modifying
    @Transactional
    @Query("DELETE FROM SubobjectiveEntryEntity e WHERE e.subobjectiveId = :subId")
    fun deleteBySubobjectiveId(@Param("subId") subId: Int)
}