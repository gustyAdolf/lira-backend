package com.lira.infrastructure.checkin.dto

import com.fasterxml.jackson.annotation.JsonFormat
import com.lira.domain.checkin.Checkin
import java.time.LocalDateTime

data class CheckinResponse(
    val id: Int?,
    val userId: Int,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    val checkinTime: LocalDateTime,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    val checkoutTime: LocalDateTime?,
    val totalHours: Double? = null
)

fun Checkin.toResponse() = CheckinResponse(
    id = id,
    userId = userId,
    checkinTime = checkinTime,
    checkoutTime = checkoutTime,
    totalHours = totalHours
)
