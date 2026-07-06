package com.lira.infrastructure.therapistschedule

import com.lira.domain.therapistschedule.TherapistScheduleException
import com.lira.infrastructure.user.entity.TherapistEntity
import jakarta.persistence.*
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

@Entity
@Table(name = "therapist_schedule_exception")
class TherapistScheduleExceptionEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "therapist_id")
    val therapist: TherapistEntity,

    @Column(name = "date")
    val date: LocalDate,

    @Column(name = "start_time")
    val startTime: LocalTime? = null,

    @Column(name = "end_time")
    val endTime: LocalTime? = null,

    @Column(name = "reason")
    val reason: String? = null,

    @Column(name = "created_at")
    val createdAt: LocalDateTime = LocalDateTime.now(),
)

fun TherapistScheduleExceptionEntity.toDomain() = TherapistScheduleException(
    id = id,
    therapistId = therapist.id,
    date = date,
    startTime = startTime,
    endTime = endTime,
    reason = reason,
)
