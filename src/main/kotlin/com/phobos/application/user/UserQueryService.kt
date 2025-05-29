package com.phobos.application.user

import com.phobos.infrastructure.user.dto.UserResponse
import com.phobos.infrastructure.user.dto.toResponse
import com.phobos.domain.user.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class UserQueryService(
    private val userRepository: UserRepository
) {
    fun getUserByEmail(email: String): UserResponse {
        val user = userRepository.findByEmail(email)
            ?: throw RuntimeException("User not found")
        return user.toResponse()
    }
}