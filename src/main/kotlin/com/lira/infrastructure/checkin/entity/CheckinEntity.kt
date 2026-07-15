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

    @Column(name = "auto_closed", nullable = false)
    var autoClosed: Boolean = false,

    @Column(name = "company_id")
    var companyId: Int? = null,
)

fun CheckinEntity.toDomain() = Checkin(
    id = id,
    userId = user.id,
    checkinTime = checkinTime,
    checkoutTime = checkoutTime,
    totalHours = totalHours,
    autoClosed = autoClosed,
    companyId = companyId
)

fun Checkin.toEntity(userEntity: UserEntity) = CheckinEntity(
    user = userEntity,
    checkinTime = checkinTime,
    checkoutTime = checkoutTime,
    autoClosed = autoClosed,
    companyId = companyId,
)