package com.lira.infrastructure.therapistschedule

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.time.LocalDate

interface JpaTherapistScheduleExceptionRepository : JpaRepository<TherapistScheduleExceptionEntity, Int> {

    fun findByTherapistId(therapistId: Int): List<TherapistScheduleExceptionEntity>

    fun findByTherapistIdAndDate(therapistId: Int, date: LocalDate): List<TherapistScheduleExceptionEntity>

    fun findByTherapistIdAndDateBetween(
        therapistId: Int,
        start: LocalDate,
        end: LocalDate
    ): List<TherapistScheduleExceptionEntity>

    @Modifying
    @Query("DELETE FROM TherapistScheduleExceptionEntity e WHERE e.therapist.id = :therapistId AND e.date = :date")
    fun deleteByTherapistIdAndDate(@Param("therapistId") therapistId: Int, @Param("date") date: LocalDate)
}
