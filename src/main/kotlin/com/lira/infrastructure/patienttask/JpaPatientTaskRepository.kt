package com.lira.infrastructure.patienttask

import org.springframework.data.jpa.repository.JpaRepository

interface JpaPatientTaskRepository : JpaRepository<PatientTaskEntity, Int> {
    fun findByPatientId(patientId: Int): List<PatientTaskEntity>
}
