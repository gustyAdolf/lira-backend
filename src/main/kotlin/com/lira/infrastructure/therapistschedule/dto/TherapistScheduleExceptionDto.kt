package com.lira.infrastructure.therapistschedule.dto

import com.lira.domain.therapistschedule.TherapistScheduleException
import java.time.LocalDate
import java.time.LocalTime

data class TherapistScheduleExceptionRequest(
    val date: LocalDate,
    val endDate: LocalDate? = null,
    val startTime: LocalTime? = null,
    val endTime: LocalTime? = null,
    val reason: String? = null,
)

data class TherapistScheduleExceptionResponse(
    val date: LocalDate,
    val startTime: LocalTime?,
    val endTime: LocalTime?,
    val reason: String?,
)

/**
 * A date range always expands to full-day exceptions, one row per date;
 * start/end time only apply when a single date (no endDate) is requested.
 */
fun TherapistScheduleExceptionRequest.toDomainList(therapistId: Int): List<TherapistScheduleException> {
    val rangeEnd = endDate
    if (rangeEnd == null) {
        return listOf(
            TherapistScheduleException(
                therapistId = therapistId,
                date = date,
                startTime = startTime,
                endTime = endTime,
                reason = reason,
            )
        )
    }
    return date.datesUntil(rangeEnd.plusDays(1)).map { d ->
        TherapistScheduleException(
            therapistId = therapistId,
            date = d,
            startTime = null,
            endTime = null,
            reason = reason,
        )
    }.toList()
}

fun TherapistScheduleException.toDto() = TherapistScheduleExceptionResponse(
    date = date,
    startTime = startTime,
    endTime = endTime,
    reason = reason,
)
