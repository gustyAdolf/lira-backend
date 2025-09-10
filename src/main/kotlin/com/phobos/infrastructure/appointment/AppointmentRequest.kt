package com.phobos.infrastructure.appointment

import com.fasterxml.jackson.annotation.JsonFormat
import com.phobos.domain.appointment.Appointment
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

fun AppointmentRequest.toDomain(): Appointment {
    return Appointment(
        therapistId = therapistId,
        userId = userId,
        mentalDisorderId = mentalDisorderId,
        appointmentDate = appointmentDate,
        appointmentDuration = appointmentDuration,
        description = description
    )
}