package com.phobos.infrastructure.appointment.dto

import com.fasterxml.jackson.annotation.JsonFormat
import com.phobos.domain.appointment.Appointment
import com.phobos.domain.mentaldisorder.MentalDisorder
import com.phobos.domain.user.Patient
import com.phobos.domain.user.Therapist
import com.phobos.infrastructure.appointment.AppointmentStatus
import java.math.BigDecimal
import java.time.LocalDateTime

data class AppointmentRequest(
    val therapistId: Int,
    val userId: Int,
    val mentalDisorderId: Int,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    val appointmentDate: LocalDateTime = LocalDateTime.now(),
    val appointmentDuration: Int,
    val description: String? = null,
    val cost: BigDecimal = BigDecimal.ZERO,
    val status: AppointmentStatus = AppointmentStatus.PENDING
)

fun AppointmentRequest.toDomain(): Appointment {
    val therapist = Therapist(id = therapistId)
    val patient = Patient(id = userId)
    val mentalDisorder = MentalDisorder(id = mentalDisorderId)
    return Appointment(
        therapist = therapist,
        patient = patient,
        mentalDisorder = mentalDisorder,
        appointmentDate = appointmentDate,
        appointmentDuration = appointmentDuration,
        description = description,
        cost = cost,
        status = status,
    )
}