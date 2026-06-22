package com.lira.domain.user

import java.time.LocalDate

sealed class User {
    abstract val id: Int
    abstract val companyId: Int
    abstract val userType: UserType
    abstract val name: String
    abstract val email: String
    abstract val password: String
    abstract val profileImagePath: String?
    abstract val telephone: String?
    abstract val address: String?
    abstract val releaseDate: LocalDate?
}