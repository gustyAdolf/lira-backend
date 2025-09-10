package com.phobos.infrastructure.user.jpa

import com.phobos.infrastructure.user.entity.CompanyEntity
import org.springframework.data.jpa.repository.JpaRepository

interface JpaCompanyRepository : JpaRepository<CompanyEntity, Int> {
}