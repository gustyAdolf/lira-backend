package com.phobos.application.checkin

import com.phobos.domain.checkin.CheckinRepository
import com.phobos.infrastructure.checkin.dto.CheckinRequest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class UpdateCheckin(
    private val checkinRepository: CheckinRepository
) {
    fun execute(checkinRequest: CheckinRequest) {
        checkinRepository.updateCheckin(
            checkinRequest.id,
            checkinRequest.checkinTime,
            checkinRequest.checkoutTime
        )
    }
}