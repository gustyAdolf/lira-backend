package com.lira.domain.progressplan

import java.time.LocalDateTime

data class SubobjectiveEntry(
    val id: Int = 0,
    val subobjectiveId: Int,
    val therapistId: Int,
    val appointmentId: Int? = null,
    val planSessionId: Int? = null,
    val entryDate: LocalDateTime,
    val valueIncrement: Int,
    val isSuccess: Boolean,
    val note: String
)
