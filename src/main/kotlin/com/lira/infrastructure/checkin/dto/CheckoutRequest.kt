package com.lira.infrastructure.checkin.dto

import com.fasterxml.jackson.annotation.JsonFormat
import java.time.LocalDateTime

data class CheckoutRequest(
    val userId: Int,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    val checkoutTime: LocalDateTime = LocalDateTime.now(),
)
