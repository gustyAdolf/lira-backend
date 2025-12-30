package com.lira.infrastructure.report.dto

import com.fasterxml.jackson.annotation.JsonFormat
import com.lira.domain.appointment.Appointment
import com.lira.domain.report.Report
import com.lira.domain.report.ReportTypeEnum
import com.lira.infrastructure.appointment.AppointmentStatus
import com.lira.infrastructure.mentaldisorder.MentalDisorderResponse
import com.lira.infrastructure.mentaldisorder.toResponse
import com.lira.infrastructure.user.dto.PatientResponse
import com.lira.infrastructure.user.dto.TherapistResponse
import com.lira.infrastructure.user.dto.toResponse
import java.math.BigDecimal
import java.time.LocalDateTime

data class ReportResponse(
    val id: Int,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    val patient: PatientResponse,
    val therapist: TherapistResponse,
    val reportType: ReportTypeEnum,
    val reportData: Map<String, Any>,
    val createdAt: LocalDateTime = LocalDateTime.now()
)

fun Report.toResponse(): ReportResponse {
    return ReportResponse(
        id = this.id,
        patient = this.patient.toResponse(),
        therapist = this.therapist.toResponse(),
        createdAt = this.createdAt,
        reportData = this.data,
        reportType = this.type
    )
}
