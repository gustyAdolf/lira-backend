package com.lira.infrastructure.progressplan.entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "subobjective_entries")
class SubobjectiveEntry(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int = 0,

    @Column(name = "subobjective_id")
    val subobjectiveId: Int,

    @Column(name = "therapist_id")
    val therapistId: Int,

    @Column(name = "entry_date")
    val entryDate: LocalDateTime,

    @Column(name = "value_increment")
    val valueIncrement: Int,

    @Column(name = "is_success")
    val isSuccess: Boolean,

    @Column(name = "note")
    val note: String,
)