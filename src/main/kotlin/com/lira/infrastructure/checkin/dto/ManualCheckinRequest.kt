package com.lira.infrastructure.checkin.dto

import com.fasterxml.jackson.annotation.JsonFormat
import com.lira.domain.checkin.Checkin
import java.time.LocalDateTime

data class ManualCheckinRequest(
    val userId: Int,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    val checkinTime: LocalDateTime,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    val checkoutTime: LocalDateTime,
    val companyId: Int? = null
)

fun ManualCheckinRequest.toDomain() = Checkin(
    userId = userId,
    checkinTime = checkinTime,
    checkoutTime = checkoutTime,
    companyId = companyId
)
