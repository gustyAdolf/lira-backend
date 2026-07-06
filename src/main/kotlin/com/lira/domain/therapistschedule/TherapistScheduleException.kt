package com.lira.domain.therapistschedule

import java.time.LocalDate
import java.time.LocalTime

data class TherapistScheduleException(
    val id: Int = 0,
    val therapistId: Int,
    val date: LocalDate,
    val startTime: LocalTime? = null,
    val endTime: LocalTime? = null,
    val reason: String? = null,
)
