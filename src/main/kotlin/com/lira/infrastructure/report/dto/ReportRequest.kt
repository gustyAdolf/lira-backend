package com.lira.infrastructure.report.dto

data class ReportRequest(
    val therapistId: Int,
    val userId: Int,
    val reportTypeId: Int,
    val dataReport: Map<String, Any>
)
