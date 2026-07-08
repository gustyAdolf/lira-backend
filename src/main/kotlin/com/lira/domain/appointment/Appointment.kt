package com.lira.domain.appointment

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
    val progressPlanId: Int? = null,
    val appointmentType: AppointmentType = AppointmentType.GENERAL,
    val therapistNotes: String? = null,
    val description: String?,
    val status: AppointmentStatus,
    val cost: BigDecimal,
    val companyId: Int? = null,
    val patientConfirmedAt: LocalDateTime? = null,
)
