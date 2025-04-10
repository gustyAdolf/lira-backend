package com.phobos.application.repository

import com.phobos.infrastructure.persistence.Company
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CompanyRepository : JpaRepository<Company, Int> {
}