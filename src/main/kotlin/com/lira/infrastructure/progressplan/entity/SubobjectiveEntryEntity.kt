package com.lira.infrastructure.progressplan.entity

import com.lira.domain.progressplan.SubobjectiveEntry
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "subobjective_entries")
class SubobjectiveEntryEntity(
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

fun SubobjectiveEntry.toEntity() = SubobjectiveEntryEntity(
    subobjectiveId = subobjectiveId,
    therapistId = therapistId,
    entryDate = entryDate,
    valueIncrement = valueIncrement,
    isSuccess = isSuccess,
    note = note
)
