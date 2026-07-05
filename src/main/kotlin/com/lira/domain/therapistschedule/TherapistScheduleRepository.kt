package com.lira.domain.therapistschedule

interface TherapistScheduleRepository {
    fun findByTherapistId(therapistId: Int): List<TherapistSchedule>
    fun replaceAll(therapistId: Int, schedules: List<TherapistSchedule>)
}
