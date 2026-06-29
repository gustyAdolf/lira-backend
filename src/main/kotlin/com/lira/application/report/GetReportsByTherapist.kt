// Archivo: com.lira.application.report.GetReportsByTherapist
package com.lira.application.report

import com.lira.domain.report.Report
import com.lira.domain.report.ReportRepository
import org.springframework.stereotype.Service

@Service
class GetReportsByTherapist(
    private val reportRepository: ReportRepository
) {
    fun execute(therapistId: Long): List<Report> {
        // Aquí podrías añadir lógica de negocio, como verificar si el
        // terapeuta tiene permisos o está activo antes de devolver los informes.

        return reportRepository.findByTherapistIdOrderByCreatedAtDesc(therapistId)
    }
}