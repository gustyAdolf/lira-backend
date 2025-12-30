package com.lira.infrastructure.reportType

import com.lira.domain.mentaldisorder.MentalDisorder
import com.lira.domain.mentaldisorder.MentalDisorderRepository
import com.lira.domain.reportType.ReportType
import com.lira.domain.reportType.ReportTypeRepository
import org.springframework.stereotype.Repository

@Repository
class JpaReportTypeRepositoryAdapter(
    private val jpaReportTypeRepository: JpaReportTypeRepository
) : ReportTypeRepository {

    override fun getAllReportTypes(): List<ReportType> {

        return jpaReportTypeRepository.findAll()
            .map { it.toDomain() }
    }
}