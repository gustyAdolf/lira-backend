package com.lira.domain.user

import com.lira.domain.appointment.Appointment

data class TherapistAvailability(
    val therapistId: Int,
    val therapistName: String,
    val therapistEmail: String,
    val futureAppointmentsCount: Int,
    val nextAppointments: List<Appointment> = emptyList(),
)
