package com.lira.domain.therapistschedule

import java.time.LocalDate

interface TherapistScheduleExceptionRepository {
    fun findByTherapistId(therapistId: Int): List<TherapistScheduleException>
    fun findByTherapistIdAndDate(therapistId: Int, date: LocalDate): List<TherapistScheduleException>
    fun findByTherapistIdAndDateBetween(therapistId: Int, start: LocalDate, end: LocalDate): List<TherapistScheduleException>
    fun saveAll(exceptions: List<TherapistScheduleException>)
    fun deleteByTherapistIdAndDate(therapistId: Int, date: LocalDate)
}
