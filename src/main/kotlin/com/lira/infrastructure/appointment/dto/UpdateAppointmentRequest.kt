package com.lira.infrastructure.appointment.dto

import com.fasterxml.jackson.annotation.JsonFormat
import com.lira.domain.appointment.AppointmentType
import java.math.BigDecimal
import java.time.LocalDateTime

data class UpdateAppointmentRequest(
    val progressPlanId: Int? = null,
    val appointmentType: AppointmentType = AppointmentType.GENERAL,
    val therapistNotes: String? = null,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    val appointmentDate: LocalDateTime,
    val appointmentDuration: Int,
    val cost: BigDecimal,
    val description: String? = null
)
