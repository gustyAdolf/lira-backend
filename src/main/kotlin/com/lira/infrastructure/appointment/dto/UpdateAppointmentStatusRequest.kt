package com.lira.infrastructure.appointment.dto

import com.lira.domain.appointment.AppointmentStatus

data class  UpdateAppointmentStatusRequest(
    val status: AppointmentStatus = AppointmentStatus.PENDING,
)
