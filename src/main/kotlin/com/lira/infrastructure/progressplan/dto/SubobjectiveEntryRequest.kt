package com.lira.infrastructure.progressplan.dto

import com.lira.domain.progressplan.SubobjectiveEntry
import java.time.LocalDateTime

data class SubobjectiveEntryRequest(
    val subobjectiveId: Int,
    val objectiveId: Int,
    val therapistId: Int,
    val appointmentId: Int? = null,
    val planSessionId: Int? = null,
    val entryDate: LocalDateTime = LocalDateTime.now(),
    val valueIncrement: Int? = 0,
    val isSuccess: Boolean? = null,
    val note: String? = null
)

fun SubobjectiveEntryRequest.toDomain(): SubobjectiveEntry = SubobjectiveEntry(
    subobjectiveId = subobjectiveId,
    therapistId = therapistId,
    appointmentId = appointmentId,
    planSessionId = planSessionId,
    entryDate = entryDate,
    valueIncrement = valueIncrement ?: 0,
    isSuccess = isSuccess ?: false,
    note = note ?: ""
)
