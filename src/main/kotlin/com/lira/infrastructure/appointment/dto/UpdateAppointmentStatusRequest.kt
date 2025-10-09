package com.lira.infrastructure.appointment.dto

import com.lira.infrastructure.appointment.AppointmentStatus

data class  UpdateAppointmentStatusRequest(
    val status: AppointmentStatus = AppointmentStatus.PENDING,
)
