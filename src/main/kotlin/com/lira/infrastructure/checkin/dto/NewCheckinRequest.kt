package com.lira.infrastructure.checkin.dto

import com.fasterxml.jackson.annotation.JsonFormat
import com.lira.domain.checkin.Checkin
import java.time.LocalDateTime

data class NewCheckinRequest(
    val userId: Int,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    val checkinTime: LocalDateTime = LocalDateTime.now(),
    val companyId: Int? = null,
)

fun NewCheckinRequest.toDomain() = Checkin(
    userId = userId,
    checkinTime = this.checkinTime,
    companyId = companyId
)