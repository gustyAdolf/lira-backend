package com.lira.infrastructure.progressplan.entity

import com.lira.domain.progressplan.Subobjective
import com.lira.domain.progressplan.SubobjectiveType
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "subobjectives")
class SubobjectiveEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int = 0,

    @Column(name = "title")
    val title: String,

    @Column(name = "description")
    val description: String?,

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    val type: SubobjectiveType,

    @Column(name = "target_value")
    val targetValue: Int?,

    @Column(name = "target_success")
    val targetSuccess: Int?,

    @Column(name = "target_fail")
    val targetFail: Int?,

    @Column(name = "current_progress")
    val currentProgress: Double,

    @Column(name = "current_value")
    val currentValue: Int = 0,

    @Column(name = "current_success_count")
    val currentSuccessCount: Int = 0,

    @Column(name = "current_fail_count")
    val currentFailCount: Int = 0,

    @Column(name = "is_completed")
    val isCompleted: Boolean = false,

    @Column(name = "created_at")
    val createdAt: LocalDateTime = LocalDateTime.now()
) {
    @ManyToOne
    @JoinColumn(name = "objective_id", nullable = false)
    lateinit var objective: ObjectiveEntity
}

fun SubobjectiveEntity.toDomain(): Subobjective =
    Subobjective(
        id = id,
        title = title,
        description = description,
        type = type,
        targetValue = targetValue,
        targetSuccess = targetSuccess,
        targetFail = targetFail,
        currentProgress = currentProgress,
        currentValue = currentValue,
        currentSuccess = currentSuccessCount,
        currentFail = currentFailCount,
        isCompleted = isCompleted,
        createdAt = createdAt
    )

fun Subobjective.toEntity(): SubobjectiveEntity =
    SubobjectiveEntity(
        id = id,
        title = title,
        description = description,
        type = type,
        targetValue = targetValue,
        targetSuccess = targetSuccess,
        targetFail = targetFail,
        currentProgress = currentProgress,
        currentValue = currentValue,
        currentSuccessCount = currentSuccess,
        currentFailCount = currentFail,
        isCompleted = isCompleted,
        createdAt = createdAt
    )
