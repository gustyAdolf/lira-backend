package com.lira.infrastructure.report.dto

import com.fasterxml.jackson.annotation.JsonFormat
import com.lira.domain.appointment.Appointment
import com.lira.domain.mentaldisorder.MentalDisorder
import com.lira.domain.report.Report
import com.lira.domain.report.ReportTypeEnum
import com.lira.domain.reportType.ReportType
import com.lira.domain.user.Patient
import com.lira.domain.user.Therapist
import com.lira.infrastructure.appointment.AppointmentStatus
import java.math.BigDecimal
import java.time.LocalDateTime

data class ReportRequest(
    val userId: Int,
    val therapistId: Int,
    val reportTypeId: Int,
    val reportData: Map<String, Any>
)

fun ReportRequest.toDomain(): Report {
    val therapist = Therapist(id = therapistId)
    val patient = Patient(id = userId)
    val reportType = ReportTypeEnum.fromId(reportTypeId) ?: throw IllegalArgumentException("Tipo de informe ID $reportTypeId no válido.")
    return Report(
        therapist = therapist,
        patient = patient,
        type = reportType,
        data = reportData,
        createdAt = LocalDateTime.now()
    )
}