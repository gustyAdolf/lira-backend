package com.lira.application.checkin

import com.lira.domain.checkin.CheckinRepository
import com.lira.infrastructure.checkin.dto.NewCheckinRequest
import com.lira.infrastructure.checkin.dto.toDomain
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class Checkin(
    private val checkinRepository: CheckinRepository
) {

    fun execute(checkinRequest: NewCheckinRequest) {
        checkinRepository.checkin(checkinRequest.toDomain())
    }
}