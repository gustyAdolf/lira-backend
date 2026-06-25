package com.lira.infrastructure.progressplan.jpa

import com.lira.infrastructure.progressplan.entity.ProgressPlanEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface JpaProgressPlanRepository : JpaRepository<ProgressPlanEntity, Int> {
    fun findAllByPatientIdAndTherapistId(patientId: Int, therapistId: Int): List<ProgressPlanEntity>
    fun findAllByPatientId(patientId: Int): List<ProgressPlanEntity>

    @Modifying
    @Query("UPDATE ProgressPlanEntity p SET p.totalProgress = :progress WHERE p.id = :id")
    fun updateTotalProgress(@Param("id") id: Int, @Param("progress") progress: Double)
}