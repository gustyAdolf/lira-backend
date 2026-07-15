package com.lira.application.checkin

import com.lira.domain.checkin.Checkin
import com.lira.domain.checkin.CheckinRepository
import com.lira.infrastructure.checkin.dto.ManualCheckinRequest
import com.lira.infrastructure.checkin.dto.toDomain
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class CreateManualCheckin(
    private val checkinRepository: CheckinRepository
) {
    fun execute(request: ManualCheckinRequest): Checkin {
        return checkinRepository.createManual(request.toDomain())
    }
}
