package com.phobos.infrastructure.appointment.dto

import com.phobos.infrastructure.appointment.AppointmentStatus

data class  UpdateAppointmentStatusRequest(
    val status: AppointmentStatus = AppointmentStatus.PENDING,
)
