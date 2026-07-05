package com.lira.domain.patienttask

import java.time.LocalDateTime

data class PatientTaskEntry(
    val id: Int = 0,
    val patientId: Int,
    val subobjectiveId: Int? = null,
    val patientTaskId: Int? = null,
    val note: String? = null,
    val emotionEntryId: Int? = null,
    val createdAt: LocalDateTime = LocalDateTime.now(),
)

data class PatientTaskJournalEntry(
    val id: Int,
    val createdAt: LocalDateTime,
    val subobjectiveId: Int?,
    val patientTaskId: Int?,
    val title: String,
    val note: String?,
    val firstEmotion: String,
    val secondEmotion: String?,
    val thirdEmotion: String?,
    val intensity: Int,
    val emotionNotes: String?,
)
