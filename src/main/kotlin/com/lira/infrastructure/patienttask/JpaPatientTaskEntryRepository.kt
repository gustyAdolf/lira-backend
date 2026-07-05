package com.lira.infrastructure.patienttask

import org.springframework.data.jpa.repository.JpaRepository

interface JpaPatientTaskEntryRepository : JpaRepository<PatientTaskEntryEntity, Int> {
    fun findByPatientId(patientId: Int): List<PatientTaskEntryEntity>
}
