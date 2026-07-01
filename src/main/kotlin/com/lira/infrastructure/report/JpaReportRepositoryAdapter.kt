package com.lira.infrastructure.report

import com.lira.domain.report.Report
import com.lira.domain.report.ReportRepository
import org.springframework.stereotype.Repository

@Repository
class JpaReportRepositoryAdapter(
    private val jpaReportDataRepository: JpaReportDataRepository
) : ReportRepository {

    override fun save(report: Report): Report =
        jpaReportDataRepository.save(report.toEntity()).toDomain()

    override fun findById(id: Int): Report? =
        jpaReportDataRepository.findById(id).orElse(null)?.toDomain()

    override fun findByTherapistId(therapistId: Int): List<Report> =
        jpaReportDataRepository.findAllByTherapistId(therapistId).map { it.toDomain() }
}
