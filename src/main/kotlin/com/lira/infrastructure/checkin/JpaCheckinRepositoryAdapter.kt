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
        val user = jpaUserRepository.getReferenceById(checkin.userId)
        val existing =
            jpaCheckinRepository.findByUserIdAndCheckoutTimeIsNull(checkin.userId)
        if (existing != null) throw CheckinException.AlreadyCheckedInException()

        jpaCheckinRepository.save(checkin.toEntity(user))
    }

    override fun checkout(userId: Int, checkoutTime: LocalDateTime) {
        val checkin = jpaCheckinRepository.findByUserIdAndCheckoutTimeIsNull(userId)
            ?: throw CheckinException.NoOpenCheckinException()
        val updated = checkin.copy(checkoutTime = checkoutTime)
        jpaCheckinRepository.save(updated)
    }

    override fun getHistory(userId: Int): List<Checkin> {
        return jpaCheckinRepository.findByUserId(userId).map { it.toDomain() }
    }

    override fun getWorkDay(
        userId: Int,
        date: LocalDate
    ): List<Checkin> {
        return jpaCheckinRepository.findByUserIdAndDate(userId, date).map { it.toDomain() }
    }

    override fun updateCheckin(
        checkinId: Int,
        checkinTime: LocalDateTime,
        checkoutTime: LocalDateTime?
    ) {
        val existing = jpaCheckinRepository.findById(checkinId).orElseThrow { CheckinException.NoCheckinExist() }

        val updatedCheckin = existing.copy(
            checkinTime = checkinTime,
            checkoutTime = checkoutTime
        )
        jpaCheckinRepository.save(updatedCheckin)
    }

    override fun deleteCheckin(checkinId: Int) {
        jpaCheckinRepository.deleteById(checkinId)
    }

}