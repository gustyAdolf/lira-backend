package com.lira.domain.report

interface ReportRepository {
    fun save(report: Report): Report
    fun findById(id: Int): Report?
    fun findByTherapistId(therapistId: Int): List<Report>
}
