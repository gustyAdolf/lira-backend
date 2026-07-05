package com.lira.infrastructure.therapistschedule

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface JpaTherapistScheduleRepository : JpaRepository<TherapistScheduleEntity, TherapistScheduleId> {

    fun findByTherapistId(therapistId: Int): List<TherapistScheduleEntity>

    @Modifying
    @Query("DELETE FROM TherapistScheduleEntity s WHERE s.therapist.id = :therapistId")
    fun deleteByTherapistId(@Param("therapistId") therapistId: Int)
}
