package com.phobos.application.user

import com.phobos.domain.user.User
import com.phobos.domain.user.UserRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class GetUsers(
    private val userRepository: UserRepository
) {
    fun execute(name: String?, mentalDisorder: String?, pageable: Pageable): Page<User> {
        return userRepository.findUsers(name, mentalDisorder, pageable);
    }
}