package com.phobos.application.checkin

import com.phobos.domain.checkin.Checkin
import com.phobos.domain.checkin.CheckinRepository
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