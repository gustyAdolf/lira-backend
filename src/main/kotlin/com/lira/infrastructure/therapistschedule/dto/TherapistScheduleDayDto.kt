package com.lira.infrastructure.therapistschedule.dto

import com.lira.domain.therapistschedule.TherapistSchedule
import java.time.LocalTime

data class TherapistScheduleDayDto(
    val dayOfWeek: Int,
    val startTime: LocalTime,
    val endTime: LocalTime,
)

fun TherapistScheduleDayDto.toDomain(therapistId: Int) = TherapistSchedule(
    therapistId = therapistId,
    dayOfWeek = dayOfWeek,
    startTime = startTime,
    endTime = endTime,
)

fun TherapistSchedule.toDto() = TherapistScheduleDayDto(
    dayOfWeek = dayOfWeek,
    startTime = startTime,
    endTime = endTime,
)
