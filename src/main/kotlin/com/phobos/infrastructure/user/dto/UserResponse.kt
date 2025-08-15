package com.phobos.infrastructure.user.dto

import com.phobos.domain.user.UserType
import java.time.LocalDate

sealed class UserResponse {
    abstract val id: Int
    abstract val userType: UserType
    abstract val name: String
    abstract val email: String
    abstract val profileImagePath: String?
    abstract val telephone: String?
    abstract val address: String?
    abstract val releaseDate: LocalDate?
}
