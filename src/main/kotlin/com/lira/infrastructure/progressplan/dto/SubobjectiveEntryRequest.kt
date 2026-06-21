package com.lira.infrastructure.progressplan.dto

import com.lira.infrastructure.progressplan.entity.SubobjectiveEntry
import java.time.LocalDateTime

data class SubobjectiveEntryRequest(
    val subobjectiveId: Int,
    val objectiveId: Int,
    val therapistId: Int,
    val entryDate: LocalDateTime = LocalDateTime.now(),
    val valueIncrement: Int,
    val isSuccess: Boolean,
    val note: String
)

fun SubobjectiveEntryRequest.toDomain(): SubobjectiveEntry = SubobjectiveEntry(
    subobjectiveId = subobjectiveId,
    therapistId = therapistId,
    entryDate = entryDate,
    valueIncrement = valueIncrement,
    isSuccess = isSuccess,
    note = note
)