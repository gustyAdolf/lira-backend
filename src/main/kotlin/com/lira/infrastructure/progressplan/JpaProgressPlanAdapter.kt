package com.lira.infrastructure.progressplan

import com.lira.domain.progressplan.*
import com.lira.infrastructure.progressplan.entity.ObjectiveEntity
import com.lira.infrastructure.progressplan.entity.ProgressPlanEntity
import com.lira.infrastructure.progressplan.entity.SubobjectiveEntryEntity
import com.lira.infrastructure.progressplan.entity.SubobjectiveEntity
import com.lira.infrastructure.progressplan.entity.toDomain
import com.lira.infrastructure.progressplan.entity.toEntity
import com.lira.infrastructure.progressplan.jpa.JpaObjectiveRepository
import com.lira.infrastructure.progressplan.jpa.JpaProgressPlanRepository
import com.lira.infrastructure.progressplan.jpa.JpaSubobjectiveEntryRepository
import com.lira.infrastructure.progressplan.jpa.JpaSubobjectiveRepository
import com.lira.infrastructure.user.entity.PatientEntity
import com.lira.infrastructure.user.entity.TherapistEntity
import jakarta.persistence.EntityManager
import org.springframework.stereotype.Repository

@Repository
class JpaProgressPlanAdapter(
    private val jpaProgressPlanRepository: JpaProgressPlanRepository,
    private val jpaObjectiveRepository: JpaObjectiveRepository,
    private val jpaSubobjectiveRepository: JpaSubobjectiveRepository,
    private val jpaSubobjectiveEntryRepository: JpaSubobjectiveEntryRepository,
    private val entityManager: EntityManager
) : ProgressPlanRepository {

    override fun save(progressPlan: ProgressPlan) {
        val patientEntity = entityManager.getReference(PatientEntity::class.java, progressPlan.patient.id)
        val therapistEntity = entityManager.getReference(TherapistEntity::class.java, progressPlan.therapist.id)
        jpaProgressPlanRepository.save(progressPlan.toEntity(patientEntity, therapistEntity))
    }

    override fun getProgressPlanById(planId: Int): ProgressPlan {
        val planEntity = jpaProgressPlanRepository.findById(planId)
            .orElseThrow { Exception("plan for id=$planId does not exist") }
        return planEntity.toDomain()
    }

    override fun getProgressPlanByPatientId(
        patientId: Int,
        therapistId: Int
    ): List<ProgressPlan> {
        return jpaProgressPlanRepository.findAllByPatientIdAndTherapistId(patientId, therapistId)
            .map { it.toDomain() }
    }

    override fun getProgressPlansByPatientId(patientId: Int): List<ProgressPlan> {
        return jpaProgressPlanRepository.findAllByPatientId(patientId).map { it.toDomain() }
    }

    override fun getProgressPlanBySubobjectiveId(subobjectiveId: Int): ProgressPlan {
        val planEntity = jpaSubobjectiveRepository.findById(subobjectiveId)
            .orElseThrow { Exception("subobjective for id=$subobjectiveId does not exist") }
            .objective.progressPlan
        return planEntity.toDomain()
    }

    override fun getObjectivesByPlanId(planId: Int): List<Objective> {
        return jpaObjectiveRepository.findByProgressPlanIdOrderByOrderIndex(planId)
            .map { it.toDomain() }
    }

    override fun getObjectiveById(id: Int): Objective {
        return jpaObjectiveRepository.findById(id)
            .orElseThrow { Exception("objective for id=$id does not exist") }
            .toDomain()
    }

    override fun getProgressPlanByObjectiveId(objectiveId: Int): ProgressPlan {
        val planEntity = jpaObjectiveRepository.findById(objectiveId)
            .orElseThrow { Exception("objective for id=$objectiveId does not exist") }
            .progressPlan
        return planEntity.toDomain()
    }

    override fun getSubobjectivesByObjectiveId(objectiveId: Int): List<Subobjective> {
        return jpaSubobjectiveRepository.findByObjectiveId(objectiveId).map { it.toDomain() }
    }

    override fun getSubobjectiveById(id: Int): Subobjective {
        return jpaSubobjectiveRepository.findById(id)
            .orElseThrow { Exception("subobjective for id=$id does not exist") }
            .toDomain()
    }

    override fun saveSubobjectiveEntry(subobjectiveEntry: SubobjectiveEntry) {
        jpaSubobjectiveEntryRepository.save(subobjectiveEntry.toEntity())
    }

    override fun saveSubobjective(subobjective: Subobjective, objectiveId: Int) {
        val objectiveEntity = entityManager.getReference(ObjectiveEntity::class.java, objectiveId)
        val entityToSave = subobjective.toEntity().apply { this.objective = objectiveEntity}
        jpaSubobjectiveRepository.save(entityToSave)
    }

    override fun sumValueBySubobjective(subobjectiveId: Int): Double {
        return jpaSubobjectiveEntryRepository.sumValueBySubobjective(subobjectiveId).toDouble()
    }

    override fun countSuccessesBySubobjective(subobjectiveId: Int): Double {
        return jpaSubobjectiveEntryRepository.countSuccessesBySubobjective(subobjectiveId).toDouble()
    }

    override fun getRecentSubobjectiveEntries(subobjectiveId: Int, limit: Int): List<SubobjectiveEntry> {
        return jpaSubobjectiveEntryRepository
            .findTop3BySubobjectiveIdOrderByEntryDateDesc(subobjectiveId)
            .take(limit)
            .map { it.toDomain() }
    }

    override fun saveObjective(objective: Objective, planId: Int) {
        val planEntity = entityManager.getReference(ProgressPlanEntity::class.java, planId)
        val entity = objective.toEntity().apply { this.progressPlan = planEntity }
        jpaObjectiveRepository.save(entity)
    }

    override fun updateObjective(objective: Objective) {
        val existing = jpaObjectiveRepository.findById(objective.id)
            .orElseThrow { Exception("objective for id=${objective.id} does not exist") }
        val updated = ObjectiveEntity(
            id = objective.id,
            title = objective.title,
            description = objective.description,
            orderIndex = objective.orderIndex,
            createdAt = objective.createdAt,
            subobjectives = existing.subobjectives
        ).apply { this.progressPlan = existing.progressPlan }
        jpaObjectiveRepository.save(updated)
    }

    override fun updateSubobjective(subobjective: Subobjective, objectiveId: Int) {
        saveSubobjective(subobjective, objectiveId)
    }

    override fun deleteObjectiveById(id: Int) {
        jpaObjectiveRepository.deleteObjective(id)
    }

    override fun deleteSubobjectiveById(id: Int) {
        jpaSubobjectiveRepository.deleteSubobjective(id)
    }

    override fun deleteEntriesBySubobjectiveId(subobjectiveId: Int) {
        jpaSubobjectiveEntryRepository.deleteBySubobjectiveId(subobjectiveId)
    }

    override fun updateTotalProgress(planId: Int, progress: Double) {
        jpaProgressPlanRepository.updateTotalProgress(planId, progress)
    }

    override fun findEntriesByPlanSessionId(planSessionId: Int): List<SubobjectiveEntry> {
        return jpaSubobjectiveEntryRepository.findByPlanSessionId(planSessionId).map { it.toDomain() }
    }

    override fun getStandaloneEntriesByPlanId(planId: Int): List<SubobjectiveEntry> {
        return jpaSubobjectiveEntryRepository.findStandaloneEntriesByPlanId(planId).map { it.toDomain() }
    }

    override fun completeSubobjective(subobjectiveId: Int, objectiveId: Int, completed: Boolean) {
        val entity = jpaSubobjectiveRepository.findById(subobjectiveId)
            .orElseThrow { Exception("subobjective for id=$subobjectiveId does not exist") }
        val newProgress = if (completed) 1.0 else 0.0
        val updated = SubobjectiveEntity(
            id = entity.id,
            title = entity.title,
            description = entity.description,
            type = entity.type,
            targetValue = entity.targetValue,
            targetSuccess = entity.targetSuccess,
            targetFail = entity.targetFail,
            currentProgress = newProgress,
            currentValue = entity.currentValue,
            currentSuccessCount = entity.currentSuccessCount,
            currentFailCount = entity.currentFailCount,
            isCompleted = completed,
            createdAt = entity.createdAt
        ).apply { objective = entity.objective }
        jpaSubobjectiveRepository.save(updated)
    }

    override fun updateSubobjectiveValues(
        subobjectiveId: Int,
        objectiveId: Int,
        currentValue: Int?,
        currentSuccess: Int?,
        currentFail: Int?
    ) {
        val entity = jpaSubobjectiveRepository.findById(subobjectiveId)
            .orElseThrow { Exception("subobjective for id=$subobjectiveId does not exist") }
        val newCurrentValue = currentValue ?: entity.currentValue
        val newCurrentSuccess = currentSuccess ?: entity.currentSuccessCount
        val newCurrentFail = currentFail ?: entity.currentFailCount
        val newProgress = if (entity.type == SubobjectiveType.QUANTITATIVE) {
            (newCurrentValue.toDouble() / (entity.targetValue ?: 1)).coerceAtMost(1.0)
        } else {
            entity.currentProgress
        }
        val updated = SubobjectiveEntity(
            id = entity.id,
            title = entity.title,
            description = entity.description,
            type = entity.type,
            targetValue = entity.targetValue,
            targetSuccess = entity.targetSuccess,
            targetFail = entity.targetFail,
            currentProgress = newProgress,
            currentValue = newCurrentValue,
            currentSuccessCount = newCurrentSuccess,
            currentFailCount = newCurrentFail,
            isCompleted = entity.isCompleted,
            createdAt = entity.createdAt
        ).apply { objective = entity.objective }
        jpaSubobjectiveRepository.save(updated)
    }

}