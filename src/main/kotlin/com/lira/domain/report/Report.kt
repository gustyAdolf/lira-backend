package com.lira.domain.report

import java.time.LocalDate

data class Report(
    val id: Int?,
    val therapistId: Int,
    val userId: Int,
    val reportTypeId: Int,
    val dataReport: String,
    val createdAt: LocalDate
)
