package com.lira.infrastructure.therapistschedule

import com.lira.domain.therapistschedule.TherapistSchedule
import com.lira.infrastructure.user.entity.TherapistEntity
import jakarta.persistence.*
import java.time.LocalTime

@Entity
@Table(name = "therapist_schedule")
@IdClass(TherapistScheduleId::class)
class TherapistScheduleEntity(
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "therapist_id")
    val therapist: TherapistEntity,

    @Id
    @Column(name = "day_of_week")
    val dayOfWeek: Int,

    @Column(name = "start_time", nullable = false)
    val startTime: LocalTime,

    @Column(name = "end_time", nullable = false)
    val endTime: LocalTime,
) {
    fun toDomain() = TherapistSchedule(
        therapistId = therapist.id,
        dayOfWeek = dayOfWeek,
        startTime = startTime,
        endTime = endTime,
    )
}

data class TherapistScheduleId(
    val therapist: Int = 0,
    val dayOfWeek: Int = 0,
) : java.io.Serializable
