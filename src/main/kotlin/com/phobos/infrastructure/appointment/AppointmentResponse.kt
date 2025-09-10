package com.phobos.infrastructure.appointment

import com.fasterxml.jackson.annotation.JsonFormat
import com.phobos.domain.appointment.Appointment
import com.phobos.infrastructure.mentaldisorder.MentalDisorderResponse
import com.phobos.infrastructure.mentaldisorder.toResponse
import com.phobos.infrastructure.user.dto.PatientResponse
import com.phobos.infrastructure.user.dto.TherapistResponse
import com.phobos.infrastructure.user.dto.toResponse
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
