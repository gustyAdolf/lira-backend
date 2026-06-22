package com.lira.infrastructure.progressplan.jpa

import com.lira.infrastructure.progressplan.entity.ProgressPlanEntity
import org.springframework.data.jpa.repository.JpaRepository

interface JpaProgressPlanRepository : JpaRepository<ProgressPlanEntity, Int> {
    fun findAllByPatientIdAndTherapistId(patientId: Int, therapistId: Int): List<ProgressPlanEntity>
    fun findAllByPatientId(patientId: Int): List<ProgressPlanEntity>


}