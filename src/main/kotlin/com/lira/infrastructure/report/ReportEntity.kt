package com.lira.infrastructure.report

import com.lira.domain.report.Report
import jakarta.persistence.*
import java.time.LocalDate

@Entity
@Table(name = "reports")
class ReportEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "reports_seq")
    @SequenceGenerator(name = "reports_seq", sequenceName = "reports_id_seq", allocationSize = 1)
    val id: Int = 0,

    @Column(name = "therapist_id")
    val therapistId: Int,

    @Column(name = "user_id")
    val userId: Int,

    @Column(name = "report_type_id")
    val reportTypeId: Int,

    @Column(name = "data_report", columnDefinition = "text")
    val dataReport: String,

    @Column(name = "created_at")
    val createdAt: LocalDate = LocalDate.now()
)

fun ReportEntity.toDomain() = Report(
    id = id,
    therapistId = therapistId,
    userId = userId,
    reportTypeId = reportTypeId,
    dataReport = dataReport,
    createdAt = createdAt
)

fun Report.toEntity() = ReportEntity(
    id = id ?: 0,
    therapistId = therapistId,
    userId = userId,
    reportTypeId = reportTypeId,
    dataReport = dataReport,
    createdAt = createdAt
)
