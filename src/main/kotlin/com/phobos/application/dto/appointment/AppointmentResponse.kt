package com.phobos.application.dto.appointment

import com.fasterxml.jackson.annotation.JsonFormat
import java.time.LocalDateTime

data class AppointmentResponse(
    val id: Int,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    val appointmentDate: LocalDateTime,
    val appointmentDuration: Int,
    val userId: Int,
    val name: String,
    val imageUrl: String?,
    val telephone: String,
    val email: String,
    val mentalDisorderId: Int,
    val mentalDisorder: String
)
