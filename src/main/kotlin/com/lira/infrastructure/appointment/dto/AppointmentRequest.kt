package com.lira.infrastructure.appointment.dto

import com.fasterxml.jackson.annotation.JsonFormat
import com.lira.domain.appointment.Appointment
import com.lira.domain.appointment.AppointmentStatus
import com.lira.domain.appointment.AppointmentType
import com.lira.domain.user.Patient
import com.lira.domain.user.Therapist
import java.math.BigDecimal
import java.time.LocalDateTime

data class AppointmentRequest(
    val therapistId: Int,
    val userId: Int,
    val progressPlanId: Int? = null,
    val appointmentType: AppointmentType = AppointmentType.GENERAL,
    val therapistNotes: String? = null,
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
    return Appointment(
        therapist = therapist,
        patient = patient,
        progressPlanId = progressPlanId,
        appointmentType = appointmentType,
        therapistNotes = therapistNotes,
        appointmentDate = appointmentDate,
        appointmentDuration = appointmentDuration,
        description = description,
        cost = cost,
        status = status,
    )
}
