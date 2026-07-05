package com.lira.domain.therapistschedule

import java.time.LocalTime

data class TherapistSchedule(
    val therapistId: Int,
    val dayOfWeek: Int,
    val startTime: LocalTime,
    val endTime: LocalTime,
)
