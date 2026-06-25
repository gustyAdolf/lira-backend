package com.lira.infrastructure.user.jpa

import com.lira.infrastructure.user.entity.PatientEntity
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository

interface JpaPatientRepository : JpaRepository<PatientEntity, Int> {

    fun findByNameContainingIgnoreCaseOrderByNameAsc(name: String, pageable: Pageable): Page<PatientEntity>

    fun findAllByOrderByNameAsc(pageable: Pageable): Page<PatientEntity>
}