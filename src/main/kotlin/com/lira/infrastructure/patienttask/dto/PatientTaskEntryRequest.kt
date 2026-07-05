package com.lira.infrastructure.patienttask.dto

import com.lira.infrastructure.emotionentry.dto.EmotionEntryRequest

data class PatientTaskEntryRequest(
    val patientId: Int,
    val subobjectiveId: Int? = null,
    val patientTaskId: Int? = null,
    val note: String? = null,
    val firstEmotion: String,
    val secondEmotion: String? = null,
    val thirdEmotion: String? = null,
    val intensity: Int,
    val emotionNotes: String? = null,
)

fun PatientTaskEntryRequest.toEmotionEntryRequest() = EmotionEntryRequest(
    userId = patientId,
    firstEmotion = firstEmotion,
    secondEmotion = secondEmotion,
    thirdEmotion = thirdEmotion,
    intensity = intensity,
    notes = emotionNotes,
)
