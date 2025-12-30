package com.lira.domain.reportType

import com.lira.infrastructure.reportType.ReportTypeEntity

data class ReportType(
    val id: Int,
    val name: String = "",
)


fun ReportType.toEntity(): ReportTypeEntity {
    return ReportTypeEntity(id, name)
}