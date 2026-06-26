package com.lira.infrastructure.user.dto

import com.lira.domain.user.TherapistAvailability
import com.lira.infrastructure.appointment.dto.AppointmentResponse
import com.lira.infrastructure.appointment.dto.toResponse

data class TherapistAvailabilityResponse(
    val therapistID: Int,
    val name: String,
    val email: String,
    val telephone: String?,
    val licenseNumber: String?,
    val profileImagePath: String?,
    val futureAppointmentsCount: Int,
    val nextAppointments: List<AppointmentResponse> = emptyList()
)

fun TherapistAvailability.toResponse(): TherapistAvailabilityResponse =
    TherapistAvailabilityResponse(
        therapistID = this.therapistId,
        name = this.therapistName,
        email = this.therapistEmail,
        telephone = this.telephone,
        licenseNumber = this.licenseNumber,
        profileImagePath = this.profileImagePath,
        futureAppointmentsCount = this.futureAppointmentsCount,
        nextAppointments = this.nextAppointments.map { it.toResponse() }
    )
