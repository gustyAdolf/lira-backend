package com.lira.infrastructure.therapistschedule

import com.lira.domain.therapistschedule.TherapistSchedule
import com.lira.domain.therapistschedule.TherapistScheduleRepository
import com.lira.infrastructure.user.entity.TherapistEntity
import jakarta.persistence.EntityManager
import jakarta.transaction.Transactional
import org.springframework.stereotype.Repository

@Repository
class JpaTherapistScheduleRepositoryAdapter(
    private val jpaRepo: JpaTherapistScheduleRepository,
    private val entityManager: EntityManager,
) : TherapistScheduleRepository {

    override fun findByTherapistId(therapistId: Int): List<TherapistSchedule> =
        jpaRepo.findByTherapistId(therapistId).map { it.toDomain() }

    @Transactional
    override fun replaceAll(therapistId: Int, schedules: List<TherapistSchedule>) {
        jpaRepo.deleteByTherapistId(therapistId)
        jpaRepo.flush()
        val therapistRef = entityManager.getReference(TherapistEntity::class.java, therapistId)
        val entities = schedules.map { s ->
            TherapistScheduleEntity(
                therapist = therapistRef,
                dayOfWeek = s.dayOfWeek,
                startTime = s.startTime,
                endTime = s.endTime,
            )
        }
        jpaRepo.saveAll(entities)
    }
}
