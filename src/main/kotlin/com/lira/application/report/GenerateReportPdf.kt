package com.lira.application.report

import com.lira.domain.report.ReportRepository
import com.lira.infrastructure.report.ReportPdfGenerator
import org.springframework.stereotype.Service

@Service
class GenerateReportPdf(
    private val repository: ReportRepository,
    private val pdfGenerator: ReportPdfGenerator
) {
    fun execute(reportId: Long): ByteArray {
        val report = repository.findById(reportId)
            ?: throw RuntimeException("Reporte con ID $reportId no encontrado")

        return pdfGenerator.generate(report)
    }
}