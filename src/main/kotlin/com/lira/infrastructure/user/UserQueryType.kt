package com.lira.infrastructure.user

import com.lira.domain.user.UserType

enum class UserQueryType {
    THERAPIST,
    PATIENT,
    COMPANY,
    ALL;

    fun toUserEntityType(): UserType {
        return UserType.valueOf(this.name)
    }
}