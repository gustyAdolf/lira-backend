package com.lira.infrastructure.user.jpa

import com.lira.infrastructure.user.entity.PatientEntity
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface JpaPatientRepository : JpaRepository<PatientEntity, Int> {

    fun findByNameContainingIgnoreCaseOrderByNameAsc(name: String, pageable: Pageable): Page<PatientEntity>

    fun findAllByOrderByNameAsc(pageable: Pageable): Page<PatientEntity>

    @Query("""
        SELECT p FROM PatientEntity p
        JOIN UserCompanyEntity uc ON uc.user.id = p.id
        WHERE uc.company.id = :companyId
        ORDER BY p.name ASC
    """)
    fun findByCompanyId(@Param("companyId") companyId: Int): List<PatientEntity>
}
