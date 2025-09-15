package com.phobos.domain.checkin

import com.phobos.infrastructure.checkin.dto.CheckinResponse
import com.phobos.infrastructure.checkin.entity.CheckinEntity
import com.phobos.infrastructure.user.entity.UserEntity
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