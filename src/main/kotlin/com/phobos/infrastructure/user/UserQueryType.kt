package com.phobos.infrastructure.user

import com.phobos.domain.user.UserType

enum class UserQueryType {
    THERAPIST,
    PATIENT,
    COMPANY,
    ALL;

    fun toUserEntityType(): UserType {
        return UserType.valueOf(this.name)
    }
}