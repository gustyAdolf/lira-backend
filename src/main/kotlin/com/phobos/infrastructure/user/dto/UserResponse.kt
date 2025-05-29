package com.phobos.infrastructure.user.dto

import com.fasterxml.jackson.annotation.JsonFormat
import com.phobos.infrastructure.mentaldisorder.MentalDisorderResponse
import com.phobos.domain.user.User
import com.phobos.domain.user.UserType
import java.time.LocalDate

data class UserResponse(
    val id: Int,
    val name: String,
    val email: String,
    val imageUrl: String?,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    val birthdate: LocalDate?,
    val telephone: String?,
    val address: String?,
    val userType: UserType,
    val mentalDisorders: List<MentalDisorderResponse>
)

fun User.toResponse(): UserResponse {
    return UserResponse(
        id = this.id,
        name = this.name,
        email = this.email,
        imageUrl = this.imageUrl,
        birthdate = this.birthdate,
        telephone = this.telephone,
        address = this.address,
        userType = this.userType,
        mentalDisorders = this.mentalDisorders.map {
            MentalDisorderResponse(
                id = it.id,
                mentalDisorder = it.name
            )
        }
    )
}
