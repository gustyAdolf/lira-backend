package com.phobos.infrastructure.checkin.jpa

import com.phobos.infrastructure.checkin.entity.CheckinEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.time.LocalDate

interface JpaCheckinRepository : JpaRepository<CheckinEntity, Int> {
    fun findByUserId(therapistId: Int): List<CheckinEntity>
    fun findByUserIdAndCheckoutTimeIsNull(therapistId: Int): CheckinEntity?

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
}