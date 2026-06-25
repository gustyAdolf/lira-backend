package com.lira.infrastructure.progressplan

import com.lira.domain.progressplan.*
import com.lira.infrastructure.progressplan.entity.ObjectiveEntity
import com.lira.infrastructure.progressplan.entity.ProgressPlanEntity
import com.lira.infrastructure.progressplan.entity.SubobjectiveEntryEntity
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
        val statsMap = buildStatsMap(planEntity.objectives.flatMap { it.subobjectives }.map { it.id })
        return planEntity.toDomain(statsMap)
    }

    override fun getProgressPlanByPatientId(
        patientId: Int,
        therapistId: Int
    ): List<ProgressPlan> {
        val plans = jpaProgressPlanRepository.findAllByPatientIdAndTherapistId(patientId, therapistId)
        val statsMap = buildStatsMap(plans.flatMap { it.objectives }.flatMap { it.subobjectives }.map { it.id })
        return plans.map { it.toDomain(statsMap) }
    }

    override fun getProgressPlansByPatientId(patientId: Int): List<ProgressPlan> {
        val plans = jpaProgressPlanRepository.findAllByPatientId(patientId)
        val statsMap = buildStatsMap(plans.flatMap { it.objectives }.flatMap { it.subobjectives }.map { it.id })
        return plans.map { it.toDomain(statsMap) }
    }

    override fun getProgressPlanBySubobjectiveId(subobjectiveId: Int): ProgressPlan {
        val planEntity = jpaSubobjectiveRepository.findById(subobjectiveId)
            .orElseThrow { Exception("subobjective for id=$subobjectiveId does not exist") }
            .objective.progressPlan
        val statsMap = buildStatsMap(planEntity.objectives.flatMap { it.subobjectives }.map { it.id })
        return planEntity.toDomain(statsMap)
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
        val statsMap = buildStatsMap(planEntity.objectives.flatMap { it.subobjectives }.map { it.id })
        return planEntity.toDomain(statsMap)
    }

    override fun getSubobjectivesByObjectiveId(objectiveId: Int): List<Subobjective> {
        val entities = jpaSubobjectiveRepository.findByObjectiveId(objectiveId)
        if (entities.isEmpty()) return emptyList()
        val stats = buildStatsMap(entities.map { it.id })
        return entities.map { entity ->
            val (v, s, f) = stats[entity.id] ?: Triple(0, 0, 0)
            entity.toDomain(currentValue = v, currentSuccess = s, currentFail = f)
        }
    }

    override fun getSubobjectiveById(id: Int): Subobjective {
        val entity = jpaSubobjectiveRepository.findById(id)
            .orElseThrow { Exception("subobjective for id=$id does not exist") }
        val (v, s, f) = buildStatsMap(listOf(id))[id] ?: Triple(0, 0, 0)
        return entity.toDomain(currentValue = v, currentSuccess = s, currentFail = f)
    }

    private fun buildStatsMap(subIds: List<Int>): Map<Int, Triple<Int, Int, Int>> {
        if (subIds.isEmpty()) return emptyMap()
        return jpaSubobjectiveEntryRepository.aggregateBySubobjectiveIds(subIds).associate { row ->
            (row[0] as Number).toInt() to Triple(
                (row[1] as Number).toInt(),
                (row[2] as Number).toInt(),
                (row[3] as Number).toInt()
            )
        }
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

}