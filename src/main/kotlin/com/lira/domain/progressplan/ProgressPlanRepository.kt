package com.lira.domain.progressplan

import com.lira.domain.progressplan.SubobjectiveEntry

interface ProgressPlanRepository {
    fun save(progressPlan: ProgressPlan)

    fun getProgressPlanById(planId: Int): ProgressPlan

    fun getProgressPlanByPatientId(patientId: Int, therapistId: Int): List<ProgressPlan>
    fun getProgressPlansByPatientId(patientId: Int): List<ProgressPlan>

    fun getProgressPlanBySubobjectiveId(subobjectiveId: Int): ProgressPlan

    fun getObjectivesByPlanId(planId: Int): List<Objective>

    fun getObjectiveById(id: Int): Objective

    fun getProgressPlanByObjectiveId(objectiveId: Int): ProgressPlan

    fun getSubobjectivesByObjectiveId(objectiveId: Int): List<Subobjective>

    fun getSubobjectiveById(id: Int): Subobjective

    fun saveSubobjectiveEntry(subobjectiveEntry: SubobjectiveEntry)

    fun saveSubobjective(subobjective: Subobjective, objectiveId: Int)

    fun sumValueBySubobjective(subobjectiveId: Int): Double

    fun countSuccessesBySubobjective(subobjectiveId: Int): Double

    fun getRecentSubobjectiveEntries(subobjectiveId: Int, limit: Int): List<SubobjectiveEntry>

    fun saveObjective(objective: Objective, planId: Int)

    fun updateObjective(objective: Objective)

    fun updateSubobjective(subobjective: Subobjective, objectiveId: Int)

    fun deleteObjectiveById(id: Int)

    fun deleteSubobjectiveById(id: Int)

    fun deleteEntriesBySubobjectiveId(subobjectiveId: Int)

    fun updateTotalProgress(planId: Int, progress: Double)

    fun findEntriesByPlanSessionId(planSessionId: Int): List<SubobjectiveEntry>
}