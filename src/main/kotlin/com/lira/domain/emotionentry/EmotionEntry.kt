package com.lira.domain.emotionentry

import com.lira.domain.user.Patient
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