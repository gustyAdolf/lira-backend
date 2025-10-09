package com.lira.application.checkin

import com.lira.domain.checkin.Checkin
import com.lira.domain.checkin.CheckinRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

@Service
@Transactional(readOnly = true)
class GetWorkDay(
    private val checkinRepository: CheckinRepository
) {
    fun execute(userId: Int, date: LocalDate): List<Checkin> {
        return checkinRepository.getWorkDay(userId, date)
    }
}