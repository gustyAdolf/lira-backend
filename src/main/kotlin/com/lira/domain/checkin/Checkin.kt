package com.lira.domain.checkin

import java.time.LocalDateTime

data class Checkin(
    val id: Int? = null,
    val userId: Int,
    val checkinTime: LocalDateTime,
    val checkoutTime: LocalDateTime? = null,
    val totalHours: Double? = null,
    val autoClosed: Boolean = false,
    val companyId: Int? = null
)