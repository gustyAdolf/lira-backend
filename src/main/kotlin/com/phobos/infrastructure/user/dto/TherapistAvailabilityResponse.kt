package com.phobos.infrastructure.user.dto

import com.phobos.domain.user.TherapistAvailability
import com.phobos.infrastructure.appointment.AppointmentResponse
import com.phobos.infrastructure.appointment.toResponse

data class TherapistAvailabilityResponse(
    val therapistID: Int,
    val name: String,
    val email: String,
    val futureAppointmentsCount: Int,
    val nextAppointments: List<AppointmentResponse> = emptyList()
)

fun TherapistAvailability.toResponse(): TherapistAvailabilityResponse =
    TherapistAvailabilityResponse(
        therapistID = this.therapistId,
        name = this.therapistName,
        email = this.therapistEmail,
        futureAppointmentsCount = this.futureAppointmentsCount,
        nextAppointments = this.nextAppointments.map { it.toResponse() }
    )
