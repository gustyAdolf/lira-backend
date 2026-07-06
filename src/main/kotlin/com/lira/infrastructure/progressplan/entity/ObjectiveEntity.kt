package com.lira.infrastructure.progressplan.entity

import com.lira.domain.progressplan.Objective
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "objectives")
class ObjectiveEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int = 0,

    @Column(name = "title")
    val title: String,

    @Column(name = "description")
    val description: String?,

    @Column(name = "order_index")
    val orderIndex: Int,

    @Column(name = "created_at")
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @OneToMany(mappedBy = "objective", cascade = [CascadeType.ALL], orphanRemoval = true)
    val subobjectives: MutableList<SubobjectiveEntity> = mutableListOf()
) {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plan_id", nullable = false)
    lateinit var progressPlan: ProgressPlanEntity
}

fun ObjectiveEntity.toDomain(): Objective =
    Objective(
        id = id,
        title = title,
        description = description,
        orderIndex = orderIndex,
        createdAt = createdAt,
        subobjectives = subobjectives.map { it.toDomain() }
    )
