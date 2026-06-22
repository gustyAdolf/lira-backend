package com.lira.infrastructure.checkin.entity

import com.lira.domain.checkin.Checkin
import com.lira.infrastructure.user.entity.UserEntity
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "user_checkin")
data class CheckinEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    val user: UserEntity,

    @Column(name = "checkin_time", nullable = false)
    var checkinTime: LocalDateTime,

    @Column(name = "checkout_time")
    var checkoutTime: LocalDateTime? = null,

    @Column(name = "total_hours", insertable = false, updatable = false)
    var totalHours: Double? = null,
)

fun CheckinEntity.toDomain() = Checkin(
    id = id,
    userId = user.id,
    checkinTime = checkinTime,
    checkoutTime = checkoutTime,
    totalHours = totalHours
)

fun Checkin.toEntity(userEntity: UserEntity) = CheckinEntity(
    user = userEntity,
    checkinTime = checkinTime,
    checkoutTime = checkoutTime,
)