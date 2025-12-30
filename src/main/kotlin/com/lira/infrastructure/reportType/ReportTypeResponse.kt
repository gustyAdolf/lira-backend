package com.lira.infrastructure.reportType

import com.lira.domain.reportType.ReportType

data class ReportTypeResponse(
    val id: Int,
    val reportType: String
)

fun ReportType.toResponse(): ReportTypeResponse {
    return ReportTypeResponse(
        id = id,
        reportType = name
    )
}