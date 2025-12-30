package com.lira.application.reportType

import com.lira.domain.mentaldisorder.MentalDisorder
import com.lira.domain.mentaldisorder.MentalDisorderRepository
import com.lira.domain.reportType.ReportType
import com.lira.domain.reportType.ReportTypeRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class GetReportTypes(
    private val reportTypeRepository: ReportTypeRepository
) {

    fun execute(): List<ReportType> {
        return reportTypeRepository.getAllReportTypes()
    }
}