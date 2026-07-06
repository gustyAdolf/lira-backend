package com.lira.infrastructure.therapistschedule

import com.lira.domain.therapistschedule.TherapistScheduleException
import com.lira.domain.therapistschedule.TherapistScheduleExceptionRepository
import com.lira.infrastructure.user.entity.TherapistEntity
import jakarta.persistence.EntityManager
import jakarta.transaction.Transactional
import org.springframework.stereotype.Repository
import java.time.LocalDate

@Repository
class JpaTherapistScheduleExceptionRepositoryAdapter(
    private val jpaRepo: JpaTherapistScheduleExceptionRepository,
    private val entityManager: EntityManager,
) : TherapistScheduleExceptionRepository {

    override fun findByTherapistId(therapistId: Int): List<TherapistScheduleException> =
        jpaRepo.findByTherapistId(therapistId).map { it.toDomain() }

    override fun findByTherapistIdAndDate(therapistId: Int, date: LocalDate): List<TherapistScheduleException> =
        jpaRepo.findByTherapistIdAndDate(therapistId, date).map { it.toDomain() }

    override fun findByTherapistIdAndDateBetween(
        therapistId: Int,
        start: LocalDate,
        end: LocalDate
    ): List<TherapistScheduleException> =
        jpaRepo.findByTherapistIdAndDateBetween(therapistId, start, end).map { it.toDomain() }

    @Transactional
    override fun saveAll(exceptions: List<TherapistScheduleException>) {
        if (exceptions.isEmpty()) return
        val therapistRef = entityManager.getReference(TherapistEntity::class.java, exceptions.first().therapistId)
        val entities = exceptions.map { e ->
            TherapistScheduleExceptionEntity(
                therapist = therapistRef,
                date = e.date,
                startTime = e.startTime,
                endTime = e.endTime,
                reason = e.reason,
            )
        }
        jpaRepo.saveAll(entities)
    }

    @Transactional
    override fun deleteByTherapistIdAndDate(therapistId: Int, date: LocalDate) {
        jpaRepo.deleteByTherapistIdAndDate(therapistId, date)
    }
}
