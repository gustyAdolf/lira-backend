package com.lira.application.checkin

import com.lira.domain.checkin.CheckinRepository
import com.lira.infrastructure.checkin.dto.NewCheckinRequest
import com.lira.infrastructure.checkin.dto.toDomain
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class Checkin(
    private val checkinRepository: CheckinRepository
) {
    private val log = LoggerFactory.getLogger(javaClass)

    fun execute(checkinRequest: NewCheckinRequest) {
        checkinRepository.checkin(checkinRequest.toDomain())
        log.info("Checkin recorded: userId=${checkinRequest.userId}, time=${checkinRequest.checkinTime}")
    }
}