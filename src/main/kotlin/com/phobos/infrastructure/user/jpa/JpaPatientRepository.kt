package com.phobos.infrastructure.user.jpa

import com.phobos.infrastructure.user.entity.PatientEntity
import org.springframework.data.jpa.repository.JpaRepository

interface JpaPatientRepository : JpaRepository<PatientEntity, Int> {
}