package com.phobos.domain.appointment

import com.phobos.infrastructure.appointment.AppointmentEntity
import com.phobos.infrastructure.mentaldisorder.MentalDisorderEntity
import com.phobos.infrastructure.user.UserEntity
import java.time.LocalDateTime

data class Appointment(
    val id: Int = 0,
    val therapistId: Int,
    val appointmentDate: LocalDateTime,
    val appointmentDuration: Int,
    val userId: Int,
    val name: String = "",
    val imageUrl: String? = "",
    val telephone: String? = "",
    val email: String = "",
    val mentalDisorderId: Int,
    val mentalDisorder: String = "",
    val description: String?
)

fun Appointment.toEntity(): AppointmentEntity =
    AppointmentEntity(
        therapistId = this.therapistId.toLong(),
        user = UserEntity.withId(this.userId),
        mentalDisorder = MentalDisorderEntity(id = this.mentalDisorderId, name = ""),
        appointmentDate = this.appointmentDate,
        appointmentDuration = this.appointmentDuration,
        description = this.description
    )
