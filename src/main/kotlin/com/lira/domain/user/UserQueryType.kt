package com.lira.domain.user

enum class UserQueryType {
    THERAPIST,
    PATIENT,
    COMPANY,
    ALL;

    fun toUserType(): UserType = UserType.valueOf(this.name)
}
