package com.lira.infrastructure.emotionentry

import org.springframework.data.domain.Sort
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.time.LocalDateTime

interface JpaEmotionEntryRepository : JpaRepository<EmotionEntryEntity, Int> {
    @Query(
        """
        SELECT e FROM EmotionEntryEntity e
        WHERE e.patient.id = :userId
        AND e.createdAt BETWEEN :startDate AND :endDate
    """
    )
    fun findByUserId(
        @Param("userId") userId: Int,
        @Param("startDate") startDate: LocalDateTime,
        @Param("endDate") endDate: LocalDateTime,
        sort: Sort
    ): List<EmotionEntryEntity>
}