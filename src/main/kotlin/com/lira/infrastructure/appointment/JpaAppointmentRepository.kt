package com.lira.infrastructure.appointment

import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.time.LocalDateTime

interface JpaAppointmentRepository : JpaRepository<AppointmentEntity, Int> {

    @Query(
        """SELECT a FROM AppointmentEntity a 
            WHERE a.therapist.id = :therapistId 
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
            WHERE a.patient.id = :patientId 
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
            JOIN UserCompanyEntity uc1 ON a.therapist.id = uc1.user.id
            JOIN UserCompanyEntity uc2 ON a.patient.id = uc2.user.id
            WHERE (uc1.company.id = :companyId OR uc2.company.id = :companyId)
            AND a.appointmentDate BETWEEN :startDate AND :endDate"""
    )
    fun findCompanyAppointmentWithDateRange(
        @Param("companyId") companyId: Int,
        @Param("startDate") startDate: LocalDateTime,
        @Param("endDate") endDate: LocalDateTime,
        sort: Sort
    ): List<AppointmentEntity>

    @Query(
        """SELECT a FROM AppointmentEntity a
            WHERE a.therapist.id = :therapistId
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
            WHERE a.patient.id = :patientId
                AND a.appointmentDate > CURRENT_DATE
            ORDER BY a.appointmentDate ASC"""
    )
    fun findNextAppointmentsForPatient(
        @Param("patientId") patientId: Int,
        pageable: Pageable
    ): List<AppointmentEntity>

    @Query(
        """SELECT DISTINCT a.patient.id FROM AppointmentEntity a WHERE a.therapist.id = :therapistId"""
    )
    fun findPatientIdsByTherapistId(@Param("therapistId") therapistId: Int): Set<Int>

    @Query(
        """
        SELECT a.therapist.id, COUNT(a)
        FROM AppointmentEntity a
        WHERE a.therapist.id IN :therapistsId AND a.appointmentDate > :now
        GROUP BY a.therapist.id
    """
    )
    fun countAppointmentsGroupedByTherapist(
        @Param("therapistsId") therapistsId: List<Int>,
        @Param("now") now: LocalDateTime
    ): List<Array<Any>>

    @Query(
        """SELECT a FROM AppointmentEntity a
            WHERE a.progressPlanId = :planId
            AND a.status = com.lira.domain.appointment.AppointmentStatus.COMPLETED
            ORDER BY a.appointmentDate DESC"""
    )
    fun findCompletedByPlanId(@Param("planId") planId: Int): List<AppointmentEntity>

}