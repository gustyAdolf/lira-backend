package com.lira.application.user

import com.lira.domain.user.ClinicSummary
import com.lira.domain.user.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class GetTherapistClinics(
    private val userRepository: UserRepository
) {
    fun execute(therapistId: Int): List<ClinicSummary> =
        userRepository.findClinicsByTherapistId(therapistId)
}
