package com.phobos.application.checkin

import com.phobos.domain.checkin.CheckinRepository
import com.phobos.infrastructure.checkin.dto.CheckinRequest
import com.phobos.infrastructure.checkin.dto.CheckoutRequest
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