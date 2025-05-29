package com.phobos.infrastructure.session

import com.fasterxml.jackson.annotation.JsonFormat
import com.phobos.domain.session.Session
import java.time.LocalDateTime

data class SessionResponse(
    val id: Int,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    val sessionDate: LocalDateTime,
    val activationLevel: Int,
    val exposureLevel: Int,
    val userId: Int,
    val mentalDisorderId: Int,
    val progress: Int,
    val therapistNotes: String,
    val userNotes: String
)

fun Session.toResponse(): SessionResponse = SessionResponse(
    id = this.id,
    sessionDate = this.sessionDate,
    activationLevel = this.activationLevel,
    exposureLevel = this.exposureLevel,
    userId = this.userId,
    mentalDisorderId = this.mentalDisorderId,
    progress = this.progress,
    therapistNotes = therapistNotes,
    userNotes = this.userNotes
)
