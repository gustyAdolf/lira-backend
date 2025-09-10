package com.phobos.application.user

import com.phobos.domain.mentaldisorder.MentalDisorder
import com.phobos.domain.user.Patient
import com.phobos.domain.user.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class GetUserMentalDisorder(
    private val userRepository: UserRepository
) {
    fun execute(userId: Int): List<MentalDisorder> {
        val user = userRepository.findById(userId)
            ?: throw RuntimeException("User with id $userId does not exist")

        return if (user is Patient) {
            user.mentalDisorders
        } else {
            emptyList()
        }

    }
}