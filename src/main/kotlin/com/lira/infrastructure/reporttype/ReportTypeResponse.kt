package com.lira.infrastructure.reporttype

import com.lira.domain.reporttype.ReportType

data class ReportTypeResponse(
    val id: Int,
    val name: String,
)

fun ReportType.toResponse(): ReportTypeResponse = ReportTypeResponse(id = id, name = name)
