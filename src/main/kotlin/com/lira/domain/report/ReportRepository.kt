package com.lira.domain.report

interface ReportRepository {
    fun save(report: Report): Report
}