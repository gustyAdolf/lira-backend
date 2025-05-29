package com.phobos.application.user

import com.phobos.domain.user.User
import com.phobos.domain.user.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class ListTherapistByCompany(
    private val userRepository: UserRepository
) {
    fun execute(companyId: Int): List<User> {
        return userRepository.findTherapistsByCompanyId(companyId)
    }
}