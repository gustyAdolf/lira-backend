package com.lira.domain.checkin

import java.time.LocalDate
import java.time.LocalDateTime

interface CheckinRepository {
    fun checkin(checkin: Checkin)
    fun checkout(userId: Int, companyId: Int?, checkoutTime: LocalDateTime)
    fun getHistory(
        userId: Int,
        from: LocalDate? = null,
        to: LocalDate? = null,
        companyId: Int? = null
    ): List<Checkin>
    fun getWorkDay(userId: Int, date: LocalDate, companyId: Int? = null): List<Checkin>
    fun updateCheckin(checkinId: Int, checkinTime: LocalDateTime, checkoutTime: LocalDateTime?)
    fun deleteCheckin(checkinId: Int)
    fun createManual(checkin: Checkin): Checkin
    fun findOpenFromPreviousDays(): List<Checkin>
    fun autoClose(checkinId: Int, checkoutTime: LocalDateTime)
}