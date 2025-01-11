package com.phobos.application.service

import com.phobos.application.dto.MentalDisorderResponse
import com.phobos.application.dto.user.UserRequest
import com.phobos.application.dto.user.UserResponse
import com.phobos.application.repository.MentalDisorderRepository
import com.phobos.application.repository.UserRepository
import com.phobos.infrastructure.mapper.UserMapper
import com.phobos.infrastructure.persistence.user.User
import com.phobos.infrastructure.persistence.user.UserDisorder
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*

@Service
class UserService(
    @Autowired private val userRepository: UserRepository,
    @Autowired private val mentalDisorderRepository: MentalDisorderRepository,
    @Value("\${user.image.path}") private val imagePath: String,
    @Value("\${user.image.preffix}") private val preffixImageName: String
) {

    @Transactional(readOnly = true)
    fun getUserByEmail(email: String): UserResponse {
        val user = userRepository.findByEmail(email)
        user?.let {
            return UserMapper.entityToResponse(user)
        }
        throw Exception("Usuario no encontrado") // TODO exception
    }


    @Transactional(readOnly = true)
    fun getUsers(name: String?, mentalDisorder: String?, pageable: Pageable): Page<UserResponse> {
        val userEntities = if (!name.isNullOrEmpty()) {
            userRepository.findByNameContainingIgnoreCase(name, pageable)
        } else if (!mentalDisorder.isNullOrEmpty()) {
            userRepository.findByUserDisordersMentalDisorderMentalDisorderContainingIgnoreCase(mentalDisorder, pageable)
        } else {
            userRepository.findAll(pageable)
        }
        return userEntities.map { UserMapper.entityToResponse(it) }
    }

    @Transactional(readOnly = true)
    fun getMentalDisordersByUserId(userId: Int): List<MentalDisorderResponse> {
        val user = userRepository.findById(userId)
        var mentalDisorders = emptyList<MentalDisorderResponse>()
        user.ifPresent {
            mentalDisorders = it.userDisorders.map {
                MentalDisorderResponse(
                    id = it.mentalDisorder.id,
                    mentalDisorder = it.mentalDisorder.mentalDisorder
                )
            }
        }
        return mentalDisorders
    }

    @Transactional
    fun createUser(userRequest: UserRequest, image: MultipartFile?) {
        val profileImagePath = image?.let { storeImage(it) }
        val mentalDisorders = mentalDisorderRepository.findAllById(userRequest.mentalDisorders)

        val user: User = UserMapper.requestToEntity(userRequest, profileImagePath)
        val savedUser = userRepository.save(user)

        val userDisorders = mentalDisorders.map {
            UserDisorder(user = savedUser, mentalDisorder = it)
        }

        savedUser.userDisorders = userDisorders
        userRepository.save(savedUser)

    }

    private fun storeImage(image: MultipartFile): String {
        val filename = "${UUID.randomUUID()}_${image.originalFilename}"
        val path = Paths.get(imagePath, filename)
        Files.copy(image.inputStream, path)
        return preffixImageName + filename
    }


}