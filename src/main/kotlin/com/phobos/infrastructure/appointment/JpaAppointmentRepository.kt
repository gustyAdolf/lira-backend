package com.phobos.infrastructure.appointment

import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.time.LocalDateTime

interface JpaAppointmentRepository : JpaRepository<AppointmentEntity, Long> {

    @Query(
        """SELECT a FROM AppointmentEntity a 
            WHERE a.therapistId = :therapistId 
            AND a.appointmentDate BETWEEN :startDate AND :endDate"""
    )
    fun findAppointmentsByTherapistIdAndDateRange(
        @Param("therapistId") therapistId: Int,
        @Param("startDate") startDate: LocalDateTime,
        @Param("endDate") endDate: LocalDateTime,
        sort: Sort
    ): List<AppointmentEntity>

    @Query(
        """SELECT a FROM AppointmentEntity a 
            WHERE a.user.id = :patientId 
            AND a.appointmentDate BETWEEN :startDate AND :endDate"""
    )
    fun findPatientAppointmentsWithDateRange(
        @Param("patientId") patientId: Int,
        @Param("startDate") startDate: LocalDateTime,
        @Param("endDate") endDate: LocalDateTime,
        sort: Sort
    ): List<AppointmentEntity>

    @Query(
        """SELECT a FROM AppointmentEntity a
            WHERE a.therapistId = :therapistId
                AND a.appointmentDate > :now
            ORDER BY a.appointmentDate ASC"""
    )
    fun findNextAppointmentsForTherapist(
        @Param("therapistId") therapistId: Int,
        @Param("now") now: LocalDateTime,
        pageable: Pageable
    ): List<AppointmentEntity>

    @Query(
        """SELECT a FROM AppointmentEntity a
            WHERE a.user.id = :patientId
                AND a.appointmentDate > CURRENT_DATE
            ORDER BY a.appointmentDate ASC"""
    )
    fun findNextAppointmentsForPatient(
        @Param("patientId") patientId: Int,
        pageable: Pageable
    ): List<AppointmentEntity>

    @Query(
        """
        SELECT a.therapistId, COUNT(a) 
        FROM AppointmentEntity a
        WHERE a.therapistId IN :therapistsId AND a.appointmentDate > :now
        GROUP BY a.therapistId
    """
    )
    fun countAppointmentsGroupedByTherapist(
        @Param("therapistsId") therapistsId: List<Int>,
        @Param("now") now: LocalDateTime
    ): List<Array<Any>>

}