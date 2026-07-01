package com.lira.infrastructure.report.dto

data class ReportDetailResponse(
    val id: Int,
    val therapistId: Int,
    val userId: Int,
    val patientName: String,
    val reportTypeId: Int,
    val reportTypeName: String,
    val createdAt: String,
    val dataReport: Map<String, Any?>
)
