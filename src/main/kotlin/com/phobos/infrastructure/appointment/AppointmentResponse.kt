package com.phobos.infrastructure.appointment

import com.fasterxml.jackson.annotation.JsonFormat
import com.phobos.domain.appointment.Appointment
import java.time.LocalDateTime

data class AppointmentResponse(
    val id: Int,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    val appointmentDate: LocalDateTime,
    val appointmentDuration: Int,
    val userId: Int,
    val name: String,
    val profileImagePath: String?,
    val telephone: String?,
    val email: String,
    val mentalDisorderId: Int,
    val mentalDisorder: String,
    val description: String?
)

fun Appointment.toResponse(): AppointmentResponse {
    return AppointmentResponse(
        id = this.id,
        appointmentDate = this.appointmentDate,
        appointmentDuration = this.appointmentDuration,
        userId = this.userId,
        name = this.name,
        profileImagePath = this.profileImagePath,
        telephone = this.telephone,
        email = this.email,
        mentalDisorderId = this.mentalDisorderId,
        mentalDisorder = this.mentalDisorder,
        description = this.description
    )
}
