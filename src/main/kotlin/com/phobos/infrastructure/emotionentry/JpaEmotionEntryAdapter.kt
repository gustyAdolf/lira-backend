package com.phobos.infrastructure.emotionentry

import com.phobos.domain.emotionentry.EmotionEntry
import com.phobos.domain.emotionentry.EmotionEntryRepository
import com.phobos.domain.emotionentry.toEntity
import com.phobos.infrastructure.user.entity.PatientEntity
import jakarta.persistence.EntityManager
import org.springframework.stereotype.Repository

@Repository
class JpaEmotionEntryAdapter(
    private val jpaEmotionRepository: JpaEmotionEntryRepository,
    private val entityManager: EntityManager
) : EmotionEntryRepository {

    override fun save(emotion: EmotionEntry) {
        val patientEntity = entityManager.getReference(PatientEntity::class.java, emotion.patient.id)
        jpaEmotionRepository.save(emotion.toEntity(patientEntity))
    }

    override fun findByUserId(userId: Int): List<EmotionEntry> {
        return jpaEmotionRepository.findByUserId(userId).map { it.toDomain() }
    }
}