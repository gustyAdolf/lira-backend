package com.lira.domain.report

import com.lira.domain.reportType.ReportType
import com.lira.domain.user.Patient
import com.lira.domain.user.Therapist
import com.lira.infrastructure.mentaldisorder.MentalDisorderEntity
import com.lira.infrastructure.report.ReportEntity
import com.lira.infrastructure.reportType.ReportTypeEntity
import com.lira.infrastructure.user.entity.PatientEntity
import com.lira.infrastructure.user.entity.TherapistEntity
import java.time.LocalDateTime

data class Report(
    val id: Int = 0,
    val therapist: Therapist,
    val patient: Patient,
    val type: ReportTypeEnum,
    val data: Map<String, Any>,
    val createdAt: LocalDateTime = LocalDateTime.now()
)

enum class ReportTypeEnum(val id: Int) {
    DERIVATION_REPORT(1),
    JUSTIFICATION_REPORT(2),
    MEDICAL_COMMUNICATION(3),
    LEGAL_REPORT(4);

    companion object {
        fun fromId(id: Int): ReportTypeEnum? = ReportTypeEnum.entries.find { it.id == id }
    }
}

fun Report.toEntity(therapistEntity: TherapistEntity,
                    patientEntity: PatientEntity,
                    report_type: ReportTypeEntity): ReportEntity =
    ReportEntity(
        therapist = therapistEntity,
        patient = patientEntity,
        reportType = report_type,
        data = this.data,
        createdAt = this.createdAt
    )
