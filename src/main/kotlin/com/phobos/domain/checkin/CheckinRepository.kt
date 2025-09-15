package com.phobos.domain.checkin

import java.time.LocalDate
import java.time.LocalDateTime

interface CheckinRepository {
    fun checkin(checkin: Checkin)
    fun checkout(userId: Int, checkoutTime: LocalDateTime)
    fun getHistory(userId: Int): List<Checkin>
    fun getWorkDay(userId: Int, date: LocalDate): List<Checkin>
    fun updateCheckin(checkinId: Int, checkinTime: LocalDateTime, checkoutTime: LocalDateTime?)
    fun deleteCheckin(checkinId: Int)
}