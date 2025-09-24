package com.phobos.infrastructure.emotionentry.dto

import com.phobos.domain.emotionentry.EmotionEntry
import java.time.LocalDateTime

data class EmotionEntryResponse(
    val userId: Int = 0,
    val createdAt: LocalDateTime,
    val firstEmotion: String,
    val secondEmotion: String?,
    val thirdEmotion: String?,
    val intensity: Int,
    val notes: String? = null
)

fun EmotionEntry.toResponse(): EmotionEntryResponse {
    return EmotionEntryResponse(
        userId = patient.id,
        createdAt = createdAt ?: LocalDateTime.now(),
        firstEmotion = firstEmotion,
        secondEmotion = secondEmotion,
        thirdEmotion = thirdEmotion,
        intensity = intensity,
        notes = notes
    )
}
