package com.lira.infrastructure.progressplan.dto

import com.lira.domain.progressplan.SubobjectiveEntry
import java.time.LocalDateTime

data class SubobjectiveEntryRequest(
    val subobjectiveId: Int,
    val objectiveId: Int,
    val therapistId: Int,
    val appointmentId: Int? = null,
    val entryDate: LocalDateTime = LocalDateTime.now(),
    val valueIncrement: Int,
    val isSuccess: Boolean,
    val note: String
)

fun SubobjectiveEntryRequest.toDomain(): SubobjectiveEntry = SubobjectiveEntry(
    subobjectiveId = subobjectiveId,
    therapistId = therapistId,
    appointmentId = appointmentId,
    entryDate = entryDate,
    valueIncrement = valueIncrement,
    isSuccess = isSuccess,
    note = note
)
