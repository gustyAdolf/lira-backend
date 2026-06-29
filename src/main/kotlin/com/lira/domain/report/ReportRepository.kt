package com.lira.domain.report

interface ReportRepository {
    fun save(report: Report): Report
    fun findByTherapistIdOrderByCreatedAtDesc(therapistId: Long): List<Report>

    fun findById(id: Long): Report?
}