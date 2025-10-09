package com.lira.domain.checkin

import com.lira.infrastructure.checkin.dto.CheckinResponse
import com.lira.infrastructure.checkin.entity.CheckinEntity
import com.lira.infrastructure.user.entity.UserEntity
import java.time.LocalDateTime

data class Checkin(
    val id: Int? = null,
    val userId: Int,
    val checkinTime: LocalDateTime,
    val checkoutTime: LocalDateTime? = null,
    val totalHours: Double? = null
)

fun Checkin.toEntity(userEntity: UserEntity) = CheckinEntity(
    user = userEntity,
    checkinTime = this.checkinTime,
    checkoutTime = this.checkoutTime,
)

fun Checkin.toResponse() = CheckinResponse(
    id = id,
    userId = userId,
    checkinTime = checkinTime,
    checkoutTime = checkoutTime,
    totalHours = totalHours
)