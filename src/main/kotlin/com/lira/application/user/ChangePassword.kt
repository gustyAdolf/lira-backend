package com.lira.application.user

import com.lira.domain.user.UserRepository
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException

@Service
@Transactional
class ChangePassword(private val userRepository: UserRepository) {
    private val log = LoggerFactory.getLogger(javaClass)
    private val encoder = BCryptPasswordEncoder()

    fun execute(userId: Int, authenticatedId: Int, currentPassword: String, newPassword: String) {
        if (userId != authenticatedId) {
            throw ResponseStatusException(HttpStatus.FORBIDDEN, "No puedes cambiar la contraseña de otro usuario")
        }
        val user = userRepository.findById(userId)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado")

        if (!encoder.matches(currentPassword, user.password)) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Contraseña actual incorrecta")
        }

        userRepository.updatePassword(userId, encoder.encode(newPassword))
        log.info("Password changed for user id=$userId")
    }
}
