package com.lira.application.report

import com.lira.domain.report.Report
import com.lira.domain.report.ReportRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class GetReport(private val reportRepository: ReportRepository) {
    fun execute(id: Int): Report = reportRepository.findById(id)
        ?: throw NoSuchElementException("Report $id not found")
}
