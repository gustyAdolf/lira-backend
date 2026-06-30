package com.lira.application.report

import com.lira.domain.report.Report
import com.lira.domain.report.ReportRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class GetReportsByTherapist(private val reportRepository: ReportRepository) {
    fun execute(therapistId: Int): List<Report> = reportRepository.findByTherapistId(therapistId)
}
