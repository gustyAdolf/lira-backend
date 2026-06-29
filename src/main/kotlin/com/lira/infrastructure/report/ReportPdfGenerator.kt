package com.lira.infrastructure.report

import com.lira.domain.report.Report


interface ReportPdfGenerator {
    fun generate(report: Report): ByteArray
}