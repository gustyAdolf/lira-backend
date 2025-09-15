package com.phobos.application.checkin

import com.phobos.domain.checkin.CheckinRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class DeleteCheckin(
    private val checkinRepository: CheckinRepository
) {
    fun execute(checkinId: Int) {
        checkinRepository.deleteCheckin(checkinId)
    }
}