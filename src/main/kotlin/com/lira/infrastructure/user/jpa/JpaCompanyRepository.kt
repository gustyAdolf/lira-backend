package com.lira.infrastructure.user.jpa

import com.lira.infrastructure.user.entity.CompanyEntity
import org.springframework.data.jpa.repository.JpaRepository

interface JpaCompanyRepository : JpaRepository<CompanyEntity, Int> {
}