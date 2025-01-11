package com.phobos.application.repository

import com.phobos.infrastructure.persistence.Appointment
import org.springframework.data.domain.Sort
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
interface AppointmentRepository : JpaRepository<Appointment, Int> {

    @Query(
        """SELECT a FROM Appointment a 
            WHERE a.therapistId = :therapistId 
            AND a.appointmentDate BETWEEN :startDate AND :endDate"""
    )
    fun findAppointmentsByTherapistIdAndDateRange(
        therapistId: Int,
        startDate: LocalDateTime,
        endDate: LocalDateTime,
        sort: Sort
    ): List<Appointment>

}