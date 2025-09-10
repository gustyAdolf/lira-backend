package com.phobos.application.user

import com.phobos.domain.user.Patient
import com.phobos.domain.user.Therapist
import com.phobos.domain.user.UserRepository
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