package com.lira.application.checkin

import com.lira.domain.checkin.Checkin
import com.lira.domain.checkin.CheckinRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class GetCheckinHistory(
    private val checkinRepository: CheckinRepository,
) {
    fun execute(userId: Int): List<Checkin> {
        return checkinRepository.getHistory(userId)
    }
}