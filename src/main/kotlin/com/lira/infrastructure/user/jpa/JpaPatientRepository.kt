package com.lira.infrastructure.user.jpa

import com.lira.infrastructure.user.entity.PatientEntity
import org.springframework.data.jpa.repository.JpaRepository

interface JpaPatientRepository : JpaRepository<PatientEntity, Int> {
}