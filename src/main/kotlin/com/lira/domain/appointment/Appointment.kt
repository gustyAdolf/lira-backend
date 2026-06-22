package com.lira.domain.appointment

import com.lira.domain.mentaldisorder.MentalDisorder
import com.lira.domain.user.Patient
import com.lira.domain.user.Therapist
import java.math.BigDecimal
import java.time.LocalDateTime

data class Appointment(
    val id: Int = 0,
    val therapist: Therapist,
    val patient: Patient,
    val appointmentDate: LocalDateTime,
    val appointmentDuration: Int,
    val mentalDisorder: MentalDisorder,
    val description: String?,
    val status: AppointmentStatus,
    val cost: BigDecimal,
)
