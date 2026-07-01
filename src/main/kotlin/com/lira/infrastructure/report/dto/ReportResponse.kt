package com.lira.infrastructure.report.dto

import com.lira.domain.report.Report
import java.time.LocalDate

data class ReportResponse(
    val id: Int,
    val therapistId: Int,
    val userId: Int,
    val patientName: String,
    val reportTypeId: Int,
    val reportTypeName: String,
    val createdAt: LocalDate
)

fun Report.toResponse(patientName: String = "", reportTypeName: String = "") = ReportResponse(
    id = id!!,
    therapistId = therapistId,
    userId = userId,
    patientName = patientName,
    reportTypeId = reportTypeId,
    reportTypeName = reportTypeName,
    createdAt = createdAt
)
