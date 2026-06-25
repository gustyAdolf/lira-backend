package com.lira.application.checkin

import com.lira.domain.checkin.CheckinRepository
import com.lira.infrastructure.checkin.dto.CheckoutRequest
import jakarta.transaction.Transactional
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
@Transactional
class Checkout(
    private val checkinRepository: CheckinRepository,
) {
    private val log = LoggerFactory.getLogger(javaClass)

    fun execute(checkinRequest: CheckoutRequest) {
        checkinRepository.checkout(
            checkinRequest.userId,
            checkinRequest.checkoutTime
        )
        log.info("Checkout recorded: userId=${checkinRequest.userId}, time=${checkinRequest.checkoutTime}")
    }
}