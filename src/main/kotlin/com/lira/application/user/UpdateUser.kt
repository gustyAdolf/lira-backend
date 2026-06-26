package com.lira.application.user

import com.lira.domain.user.Company
import com.lira.domain.user.Patient
import com.lira.domain.user.Therapist
import com.lira.domain.user.User
import com.lira.domain.user.UserRepository
import com.lira.infrastructure.user.dto.UserRequest
import com.lira.infrastructure.user.dto.toDomain
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.server.ResponseStatusException
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*

@Service
@Transactional
class UpdateUser(
    private val userRepository: UserRepository,
    @Value("\${user.image.path}") private val imagePath: String,
    @Value("\${user.image.preffix}") private val preffixImageName: String
) {
    private val log = LoggerFactory.getLogger(javaClass)

    fun execute(pathId: Int, authenticatedId: Int, request: UserRequest, image: MultipartFile?): User {
        if (pathId != authenticatedId) {
            throw ResponseStatusException(HttpStatus.FORBIDDEN, "No puedes editar el perfil de otro usuario")
        }
        val existing = userRepository.findById(pathId)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado")

        val newImagePath = if (image != null) storeImage(image) else existing.profileImagePath

        val updated = when (val user = request.toDomain(newImagePath ?: "${preffixImageName}default.png", existing.password)) {
            is Patient -> user.copy(id = pathId, companyId = existing.companyId)
            is Therapist -> user.copy(id = pathId, companyId = existing.companyId)
            is Company -> user.copy(id = pathId, companyId = existing.companyId)
            else -> user
        }

        log.info("User updated: id=$pathId")
        return userRepository.updateUser(updated)
    }

    private fun storeImage(image: MultipartFile): String {
        val filename = "${UUID.randomUUID()}_${image.originalFilename?.take(6)}"
        val path = Paths.get(imagePath, filename)
        Files.copy(image.inputStream, path)
        return preffixImageName + filename
    }
}
