package com.lira.infrastructure.patienttask.dto

import com.fasterxml.jackson.annotation.JsonFormat
import com.lira.domain.patienttask.PatientTaskJournalEntry
import java.time.LocalDateTime

data class PatientTaskJournalResponse(
    val id: Int,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
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

fun PatientTaskJournalEntry.toResponse() = PatientTaskJournalResponse(
    id = id,
    createdAt = createdAt,
    subobjectiveId = subobjectiveId,
    patientTaskId = patientTaskId,
    title = title,
    note = note,
    firstEmotion = firstEmotion,
    secondEmotion = secondEmotion,
    thirdEmotion = thirdEmotion,
    intensity = intensity,
    emotionNotes = emotionNotes,
)
