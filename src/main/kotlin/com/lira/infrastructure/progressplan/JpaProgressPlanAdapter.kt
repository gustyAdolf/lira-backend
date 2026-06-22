package com.lira.infrastructure.progressplan

import com.lira.domain.progressplan.*
import com.lira.infrastructure.progressplan.entity.ObjectiveEntity
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

    override fun getProgressPlanByPatientId(
        patientId: Int,
        therapistId: Int
    ): List<ProgressPlan> {
        return jpaProgressPlanRepository.findAllByPatientIdAndTherapistId(patientId, therapistId).map { it.toDomain() }
    }

    override fun getProgressPlansByPatientId(patientId: Int): List<ProgressPlan> {
        return jpaProgressPlanRepository.findAllByPatientId(patientId).map { it.toDomain() }
    }


    override fun getProgressPlanBySubobjectiveId(subobjectiveId: Int): ProgressPlan {
        return jpaSubobjectiveRepository.findById(subobjectiveId)
            .orElseThrow { Exception("subobjective for id=$subobjectiveId does not exist") }
            .objective.progressPlan.toDomain()
    }

    override fun getObjectivesByPlanId(planId: Int): List<Objective> {
        return jpaObjectiveRepository.findByProgressPlanIdOrderByOrderIndex(planId)
            .map { it.toDomain() }
    }

    override fun getSubobjectivesByObjectiveId(objectiveId: Int): List<Subobjective> {
        return jpaSubobjectiveRepository.findByObjectiveId(objectiveId).map { it.toDomain() }
    }

    override fun getSubobjectiveById(id: Int): Subobjective {
        return jpaSubobjectiveRepository.findById(id)
            .orElseThrow { Exception("subobjective for id=$id does not exist") }.toDomain()
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

}