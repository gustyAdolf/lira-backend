package com.lira.infrastructure.checkin

import com.lira.domain.checkin.Checkin
import com.lira.domain.checkin.CheckinRepository
import com.lira.infrastructure.checkin.entity.toEntity
import com.lira.domain.exceptions.CheckinException
import com.lira.infrastructure.checkin.entity.toDomain
import com.lira.infrastructure.checkin.jpa.JpaCheckinRepository
import com.lira.infrastructure.user.jpa.JpaUserRepository
import org.springframework.stereotype.Repository
import java.time.LocalDate
import java.time.LocalDateTime

@Repository
class JpaCheckinRepositoryAdapter(
    private val jpaCheckinRepository: JpaCheckinRepository,
    private val jpaUserRepository: JpaUserRepository
) : CheckinRepository {

    override fun checkin(checkin: Checkin) {
        val existing =
            jpaCheckinRepository.findOpenByUserIdAndCompanyId(checkin.userId, checkin.companyId)
        if (existing != null) throw CheckinException.AlreadyCheckedInException()

        ensureValidRange(checkin.checkinTime, checkin.checkoutTime)
        ensureNoOverlap(
            checkin.userId, checkin.companyId, checkin.checkinTime, checkin.checkoutTime,
            excludeId = NO_EXCLUDE
        )

        val user = jpaUserRepository.getReferenceById(checkin.userId)
        jpaCheckinRepository.save(checkin.toEntity(user))
    }

    override fun checkout(userId: Int, companyId: Int?, checkoutTime: LocalDateTime) {
        val checkin = jpaCheckinRepository.findOpenByUserIdAndCompanyId(userId, companyId)
            ?: throw CheckinException.NoOpenCheckinException()

        ensureValidRange(checkin.checkinTime, checkoutTime)
        ensureNoOverlap(userId, companyId, checkin.checkinTime, checkoutTime, excludeId = checkin.id)

        val updated = checkin.copy(checkoutTime = checkoutTime)
        jpaCheckinRepository.save(updated)
    }

    override fun getHistory(userId: Int, from: LocalDate?, to: LocalDate?, companyId: Int?): List<Checkin> {
        if (from != null && to != null && companyId != null) {
            return jpaCheckinRepository.findByUserIdAndCompanyIdAndDateBetween(userId, companyId, from, to)
                .map { it.toDomain() }
        }
        if (from != null && to != null) {
            return jpaCheckinRepository.findByUserIdAndDateBetween(userId, from, to).map { it.toDomain() }
        }
        return jpaCheckinRepository.findByUserId(userId).map { it.toDomain() }
    }

    override fun getWorkDay(userId: Int, date: LocalDate, companyId: Int?): List<Checkin> {
        if (companyId != null) {
            return jpaCheckinRepository.findByUserIdAndCompanyIdAndDate(userId, companyId, date)
                .map { it.toDomain() }
        }
        return jpaCheckinRepository.findByUserIdAndDate(userId, date).map { it.toDomain() }
    }

    override fun updateCheckin(
        checkinId: Int,
        checkinTime: LocalDateTime,
        checkoutTime: LocalDateTime?
    ) {
        val existing = jpaCheckinRepository.findById(checkinId).orElseThrow { CheckinException.NoCheckinExist() }

        ensureValidRange(checkinTime, checkoutTime)
        ensureNoOverlap(existing.user.id, existing.companyId, checkinTime, checkoutTime, excludeId = checkinId)

        val updatedCheckin = existing.copy(
            checkinTime = checkinTime,
            checkoutTime = checkoutTime
        )
        jpaCheckinRepository.save(updatedCheckin)
    }

    override fun deleteCheckin(checkinId: Int) {
        jpaCheckinRepository.deleteById(checkinId)
    }

    override fun createManual(checkin: Checkin): Checkin {
        val checkoutTime = requireNotNull(checkin.checkoutTime) {
            "El registro manual requiere hora de salida"
        }

        ensureValidRange(checkin.checkinTime, checkoutTime)
        ensureNoOverlap(
            checkin.userId, checkin.companyId, checkin.checkinTime, checkoutTime,
            excludeId = NO_EXCLUDE
        )

        val user = jpaUserRepository.getReferenceById(checkin.userId)
        val saved = jpaCheckinRepository.save(checkin.toEntity(user))
        return saved.toDomain()
    }

    override fun findOpenFromPreviousDays(): List<Checkin> {
        val cutoff = LocalDate.now().atStartOfDay()
        return jpaCheckinRepository.findByCheckoutTimeIsNullAndCheckinTimeBefore(cutoff).map { it.toDomain() }
    }

    override fun autoClose(checkinId: Int, checkoutTime: LocalDateTime) {
        val existing = jpaCheckinRepository.findById(checkinId).orElseThrow { CheckinException.NoCheckinExist() }
        val updated = existing.copy(checkoutTime = checkoutTime, autoClosed = true)
        jpaCheckinRepository.save(updated)
    }

    private fun ensureValidRange(checkinTime: LocalDateTime, checkoutTime: LocalDateTime?) {
        ensureNotFuture(checkinTime)
        if (checkoutTime != null) {
            ensureNotFuture(checkoutTime)
            if (!checkoutTime.isAfter(checkinTime)) throw CheckinException.InvalidCheckinRangeException()
        }
    }

    private fun ensureNotFuture(time: LocalDateTime) {
        if (time.isAfter(LocalDateTime.now())) throw CheckinException.FutureCheckinException()
    }

    private fun ensureNoOverlap(
        userId: Int,
        companyId: Int?,
        checkinTime: LocalDateTime,
        checkoutTime: LocalDateTime?,
        excludeId: Int
    ) {
        val rangeEnd = checkoutTime ?: LocalDateTime.now()
        val conflict = jpaCheckinRepository
            .findOverlapping(userId, companyId, checkinTime, rangeEnd, excludeId)
            .firstOrNull() ?: return

        val rangeDescription = "${conflict.checkinTime} - ${conflict.checkoutTime?.toString() ?: "en curso"}"
        throw CheckinException.OverlappingCheckinException(conflict.id, rangeDescription)
    }

    companion object {
        private const val NO_EXCLUDE = -1
    }
}
