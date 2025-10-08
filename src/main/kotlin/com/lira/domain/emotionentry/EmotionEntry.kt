package com.lira.domain.emotionentry

import com.lira.domain.user.Patient
import com.lira.infrastructure.emotionentry.EmotionEntryEntity
import com.lira.infrastructure.user.entity.PatientEntity
import java.time.LocalDateTime

data class EmotionEntry(
    val id: Int? = null,
    val patient: Patient,
    val firstEmotion: String,
    val secondEmotion: String? = null,
    val thirdEmotion: String? = null,
    val intensity: Int,
    val notes: String? = null,
    val createdAt: LocalDateTime? = null,
)

fun EmotionEntry.toEntity(patientEntity: PatientEntity) =
    EmotionEntryEntity(
        patient = patientEntity,
        firstEmotion = firstEmotion,
        secondEmotion = secondEmotion,
        thirdEmotion = thirdEmotion,
        intensity = intensity,
        notes = notes,
        createdAt = this.createdAt ?: LocalDateTime.now()
    )