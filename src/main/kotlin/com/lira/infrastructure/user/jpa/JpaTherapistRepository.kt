package com.lira.infrastructure.user.jpa

import com.lira.infrastructure.user.entity.TherapistEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface JpaTherapistRepository : JpaRepository<TherapistEntity, Int> {

    @Query("""
        SELECT t FROM TherapistEntity t
        JOIN UserCompanyEntity uc ON uc.user.id = t.id
        WHERE uc.company.id = :companyId
    """)
    fun findByCompanyId(@Param("companyId") companyId: Int): List<TherapistEntity>
}