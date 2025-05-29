package com.phobos.domain.session

import com.phobos.infrastructure.session.SessionEntity
import java.time.LocalDateTime

data class Session(
    val id: Int = 0,
    val sessionDate: LocalDateTime = LocalDateTime.now(),
    val activationLevel: Int,
    val exposureLevel: Int,
    val userId: Int,
    val mentalDisorderId: Int,
    val progress: Int,
    val therapistNotes: String,
    val userNotes: String
)

fun Session.toEntity(): SessionEntity =
    SessionEntity(
        sessionDate = sessionDate,
        activationLevel = activationLevel,
        exposureLevel = exposureLevel,
        userId = userId,
        mentalDisorderId = mentalDisorderId,
        progress = progress,
        therapistNotes = therapistNotes,
        userNotes = userNotes
    )
