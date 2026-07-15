package com.lira.infrastructure.checkin.jpa

import com.lira.infrastructure.checkin.entity.CheckinEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.time.LocalDate
import java.time.LocalDateTime

interface JpaCheckinRepository : JpaRepository<CheckinEntity, Int> {
    fun findByUserId(therapistId: Int): List<CheckinEntity>

    /**
     * Sesión abierta del usuario para una clínica concreta ("bucket" exacto:
     * `companyId = null` es su propio bucket, no "cualquier clínica"). Cada
     * clínica lleva su propio reloj de fichaje independiente.
     */
    @Query(
        """
        SELECT c FROM CheckinEntity c
        WHERE c.user.id = :userId
        AND ((:companyId IS NULL AND c.companyId IS NULL) OR c.companyId = :companyId)
        AND c.checkoutTime IS NULL
    """
    )
    fun findOpenByUserIdAndCompanyId(
        @Param("userId") userId: Int,
        @Param("companyId") companyId: Int?
    ): CheckinEntity?

    @Query(
        """
        SELECT c FROM CheckinEntity c
        WHERE c.user.id = :userId
        AND DATE(c.checkinTime) = :date
        order by c.checkinTime asc
    """
    )
    fun findByUserIdAndDate(
        @Param("userId") userId: Int,
        @Param("date") date: LocalDate
    ): List<CheckinEntity>

    @Query(
        """
        SELECT c FROM CheckinEntity c
        WHERE c.user.id = :userId
        AND c.companyId = :companyId
        AND DATE(c.checkinTime) = :date
        order by c.checkinTime asc
    """
    )
    fun findByUserIdAndCompanyIdAndDate(
        @Param("userId") userId: Int,
        @Param("companyId") companyId: Int,
        @Param("date") date: LocalDate
    ): List<CheckinEntity>

    @Query(
        """
        SELECT c FROM CheckinEntity c
        WHERE c.user.id = :userId
        AND DATE(c.checkinTime) BETWEEN :from AND :to
        order by c.checkinTime asc
    """
    )
    fun findByUserIdAndDateBetween(
        @Param("userId") userId: Int,
        @Param("from") from: LocalDate,
        @Param("to") to: LocalDate
    ): List<CheckinEntity>

    @Query(
        """
        SELECT c FROM CheckinEntity c
        WHERE c.user.id = :userId
        AND c.companyId = :companyId
        AND DATE(c.checkinTime) BETWEEN :from AND :to
        order by c.checkinTime asc
    """
    )
    fun findByUserIdAndCompanyIdAndDateBetween(
        @Param("userId") userId: Int,
        @Param("companyId") companyId: Int,
        @Param("from") from: LocalDate,
        @Param("to") to: LocalDate
    ): List<CheckinEntity>

    /**
     * Solapamiento acotado a la misma clínica ("bucket" exacto, igual que
     * [findOpenByUserIdAndCompanyId]) — dos sesiones de clínicas distintas
     * pueden solaparse en el tiempo a propósito (relojes independientes).
     */
    @Query(
        """
        SELECT c FROM CheckinEntity c
        WHERE c.user.id = :userId
        AND c.id <> :excludeId
        AND ((:companyId IS NULL AND c.companyId IS NULL) OR c.companyId = :companyId)
        AND c.checkinTime < :end
        AND (c.checkoutTime IS NULL OR c.checkoutTime > :start)
    """
    )
    fun findOverlapping(
        @Param("userId") userId: Int,
        @Param("companyId") companyId: Int?,
        @Param("start") start: LocalDateTime,
        @Param("end") end: LocalDateTime,
        @Param("excludeId") excludeId: Int
    ): List<CheckinEntity>

    fun findByCheckoutTimeIsNullAndCheckinTimeBefore(cutoff: LocalDateTime): List<CheckinEntity>
}
