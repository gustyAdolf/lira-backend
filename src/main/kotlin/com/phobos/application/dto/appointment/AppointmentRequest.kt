package com.phobos.application.dto.appointment

import com.fasterxml.jackson.annotation.JsonFormat
import java.time.LocalDateTime

data class AppointmentRequest(
    val therapistId: Int,
    val userId: Int,
    val mentalDisorderId: Int,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    val appointmentDate: LocalDateTime = LocalDateTime.now(),
    val appointmentDuration: Int,
    val description: String? = null,
)
