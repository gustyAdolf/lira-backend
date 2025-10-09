package com.lira.application.checkin

import com.lira.domain.checkin.CheckinRepository
import com.lira.infrastructure.checkin.dto.CheckoutRequest
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
@Transactional
class Checkout(
    private val checkinRepository: CheckinRepository,
) {
    fun execute(checkinRequest: CheckoutRequest) {
        checkinRepository.checkout(
            checkinRequest.userId,
            checkinRequest.checkoutTime
        )
    }
}