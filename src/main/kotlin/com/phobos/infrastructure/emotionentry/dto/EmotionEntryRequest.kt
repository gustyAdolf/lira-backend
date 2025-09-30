package com.phobos.infrastructure.emotionentry.dto

import com.phobos.domain.emotionentry.EmotionEntry
import com.phobos.domain.user.Patient

data class EmotionEntryRequest(
    val userId: Int,
    val firstEmotion: String,
    val secondEmotion: String?,
    val thirdEmotion: String?,
    val intensity: Int,
    val notes: String? = null
)

fun EmotionEntryRequest.toDomain(): EmotionEntry {
    val patient = Patient(id = userId)
    return EmotionEntry(
        patient = patient,
        firstEmotion = firstEmotion,
        secondEmotion = secondEmotion,
        thirdEmotion = thirdEmotion,
        intensity = intensity,
        notes = notes
    )
}
