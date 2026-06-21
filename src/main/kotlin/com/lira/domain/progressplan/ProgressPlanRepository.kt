package com.lira.domain.progressplan

import com.lira.infrastructure.progressplan.entity.SubobjectiveEntry

interface ProgressPlanRepository {
    fun save(progressPlan: ProgressPlan)

    fun getProgressPlanByPatientId(patientId: Int, therapistId: Int): List<ProgressPlan>

    fun getProgressPlanBySubobjectiveId(subobjectiveId: Int): ProgressPlan

    fun getObjectivesByPlanId(planId: Int): List<Objective>

    fun getSubobjectivesByObjectiveId(objectiveId: Int): List<Subobjective>

    fun getSubobjectiveById(id: Int): Subobjective

    fun saveSubobjectiveEntry(subobjectiveEntry: SubobjectiveEntry)

    fun saveSubobjective(subobjective: Subobjective, objectiveId: Int)

    fun sumValueBySubobjective(subobjectiveId: Int): Double

    fun countSuccessesBySubobjective(subobjectiveId: Int): Double
}