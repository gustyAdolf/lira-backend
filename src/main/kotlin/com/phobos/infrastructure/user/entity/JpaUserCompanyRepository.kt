package com.phobos.infrastructure.user.entity

import com.phobos.infrastructure.user.UserCompanyEntity
import org.springframework.data.jpa.repository.JpaRepository

interface JpaUserCompanyRepository : JpaRepository<UserCompanyEntity, Int> {
}