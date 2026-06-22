package com.lira.domain.session

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
