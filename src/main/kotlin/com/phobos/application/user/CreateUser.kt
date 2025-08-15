package com.phobos.application.user

import com.phobos.domain.user.UserRepository
import com.phobos.infrastructure.user.dto.*
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*

@Service
@Transactional
class CreateUser(
    private val userRepository: UserRepository,
    @Value("\${user.image.path}") private val imagePath: String,
    @Value("\${user.image.preffix}") private val preffixImageName: String
) {
    fun execute(request: UserRequest, userImage: MultipartFile?) {
        val passwordEncoder = BCryptPasswordEncoder()
        val userImagePath = storageImage(userImage)

        when (request) {
            is PatientRequest -> {
                userRepository.savePatient(request.toDomain(userImagePath, passwordEncoder.encode(request.password)))
            }

            is TherapistRequest -> {
                userRepository.saveTherapist(request.toDomain(userImagePath, passwordEncoder.encode(request.password)))
            }

            is CompanyRequest -> {
                TODO()
            }
        }
    }

    private fun storageImage(image: MultipartFile?): String {
        if (image == null) {
            return "${preffixImageName}default.png"
        }
        val filename = "${UUID.randomUUID()}_${image.originalFilename}"
        val path = Paths.get(imagePath, filename)
        Files.copy(image.inputStream, path)
        return preffixImageName + filename
    }
}