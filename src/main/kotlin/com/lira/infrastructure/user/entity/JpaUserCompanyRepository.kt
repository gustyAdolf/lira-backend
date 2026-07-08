package com.lira.infrastructure.user.entity

import com.lira.infrastructure.user.UserCompanyEntity
import org.springframework.data.jpa.repository.JpaRepository

interface JpaUserCompanyRepository : JpaRepository<UserCompanyEntity, Int> {
    fun findByUserId(userId: Int): List<UserCompanyEntity>
}