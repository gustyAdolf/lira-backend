package com.lira.infrastructure.progressplan.dto

import com.fasterxml.jackson.annotation.JsonFormat
import com.lira.domain.progressplan.SubobjectiveEntry
import java.time.LocalDateTime

data class SubobjectiveEntryResponse(
    val id: Int,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    val entryDate: LocalDateTime,
    val valueIncrement: Int,
    val isSuccess: Boolean,
    val note: String,
    val appointmentId: Int? = null
)

fun SubobjectiveEntry.toEntryResponse() = SubobjectiveEntryResponse(
    id = id,
    entryDate = entryDate,
    valueIncrement = valueIncrement,
    isSuccess = isSuccess,
    note = note,
    appointmentId = appointmentId
)
