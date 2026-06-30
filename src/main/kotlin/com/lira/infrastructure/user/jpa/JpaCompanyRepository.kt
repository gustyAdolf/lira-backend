package com.lira.infrastructure.user.jpa

import com.lira.infrastructure.user.entity.CompanyEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface JpaCompanyRepository : JpaRepository<CompanyEntity, Int> {
    @Query(value = "SELECT company_id FROM user_company WHERE user_id = :therapistId LIMIT 1", nativeQuery = true)
    fun findCompanyIdByTherapistId(@Param("therapistId") therapistId: Int): Int?
}