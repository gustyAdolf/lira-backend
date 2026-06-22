package com.lira.infrastructure.appointment.dto

import com.fasterxml.jackson.annotation.JsonFormat
import com.lira.domain.appointment.Appointment
import com.lira.domain.appointment.AppointmentStatus
import com.lira.infrastructure.mentaldisorder.MentalDisorderResponse
import com.lira.infrastructure.mentaldisorder.toResponse
import com.lira.infrastructure.user.dto.PatientResponse
import com.lira.infrastructure.user.dto.TherapistResponse
import com.lira.infrastructure.user.dto.toResponse
import java.math.BigDecimal
import java.time.LocalDateTime

data class AppointmentResponse(
    val id: Int,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    val appointmentDate: LocalDateTime,
    val appointmentDuration: Int,
    val patient: PatientResponse,
    val therapist: TherapistResponse,
    val mentalDisorder: MentalDisorderResponse,
    val description: String?,
    val status: AppointmentStatus,
    val cost: BigDecimal
)

fun Appointment.toResponse(): AppointmentResponse {
    return AppointmentResponse(
        id = this.id,
        appointmentDate = this.appointmentDate,
        appointmentDuration = this.appointmentDuration,
        patient = this.patient.toResponse(),
        therapist = this.therapist.toResponse(),
        mentalDisorder = this.mentalDisorder.toResponse(),
        description = this.description,
        status = this.status,
        cost = this.cost
    )
}
