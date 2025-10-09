package com.lira.application.user

import com.lira.domain.user.Therapist
import com.lira.domain.user.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class ListTherapistByCompany(
    private val userRepository: UserRepository
) {
    fun execute(companyId: Int): List<Therapist> {
        return userRepository.findTherapistsByCompanyId(companyId)
    }
}