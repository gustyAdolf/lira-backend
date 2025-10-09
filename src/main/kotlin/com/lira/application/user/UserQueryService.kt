package com.lira.application.user

import com.lira.domain.user.User
import com.lira.domain.user.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class UserQueryService(
    private val userRepository: UserRepository
) {
    fun getUserByEmail(email: String): User {
        return userRepository.findByEmail(email)
            ?: throw RuntimeException("User not found")
    }
}