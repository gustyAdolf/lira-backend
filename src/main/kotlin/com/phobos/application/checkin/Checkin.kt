package com.phobos.application.checkin

import com.phobos.domain.checkin.CheckinRepository
import com.phobos.infrastructure.checkin.dto.NewCheckinRequest
import com.phobos.infrastructure.checkin.dto.toDomain
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