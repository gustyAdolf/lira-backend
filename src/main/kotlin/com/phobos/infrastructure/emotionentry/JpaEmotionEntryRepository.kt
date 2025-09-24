package com.phobos.infrastructure.emotionentry

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface JpaEmotionEntryRepository : JpaRepository<EmotionEntryEntity, Int> {
    @Query(
        """
        SELECT e FROM EmotionEntryEntity e
        WHERE e.patient.id = :userId
        ORDER BY e.createdAt DESC
    """
    )
    fun findByUserId(@Param("userId") userId: Int): List<EmotionEntryEntity>
}