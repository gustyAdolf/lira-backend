package com.phobos.infrastructure.checkin.dto

import com.fasterxml.jackson.annotation.JsonFormat
import com.phobos.domain.checkin.Checkin
import java.time.LocalDateTime

data class CheckinRequest(
    val id: Int = 0,
    val userId: Int,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    val checkinTime: LocalDateTime,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    val checkoutTime: LocalDateTime?
)

fun CheckinRequest.toDomain() = Checkin(
    id = this.id,
    userId = userId,
    checkinTime = this.checkinTime,
    checkoutTime = this.checkoutTime
)
