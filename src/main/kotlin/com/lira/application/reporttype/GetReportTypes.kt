package com.lira.application.reporttype

import com.lira.domain.reporttype.ReportType
import com.lira.domain.reporttype.ReportTypeRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class GetReportTypes(
    private val reportTypeRepository: ReportTypeRepository
) {
    fun execute(): List<ReportType> = reportTypeRepository.getAllReportTypes()
}
