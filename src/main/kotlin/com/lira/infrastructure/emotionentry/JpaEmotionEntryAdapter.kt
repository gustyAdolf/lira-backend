package com.lira.infrastructure.emotionentry

import com.lira.domain.emotionentry.EmotionEntry
import com.lira.domain.emotionentry.EmotionEntryRepository
import com.lira.infrastructure.emotionentry.toEntity
import com.lira.infrastructure.user.entity.PatientEntity
import jakarta.persistence.EntityManager
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
class JpaEmotionEntryAdapter(
    private val jpaEmotionRepository: JpaEmotionEntryRepository,
    private val entityManager: EntityManager
) : EmotionEntryRepository {

    override fun save(emotion: EmotionEntry): EmotionEntry {
        val patientEntity = entityManager.getReference(PatientEntity::class.java, emotion.patient.id)
        return jpaEmotionRepository.save(emotion.toEntity(patientEntity)).toDomain()
    }

    override fun findByUserId(
        userId: Int,
        startDate: LocalDateTime,
        endDate: LocalDateTime,
        sort: Sort
    ): List<EmotionEntry> {
        return jpaEmotionRepository.findByUserId(userId, startDate, endDate, sort).map { it.toDomain() }
    }
}