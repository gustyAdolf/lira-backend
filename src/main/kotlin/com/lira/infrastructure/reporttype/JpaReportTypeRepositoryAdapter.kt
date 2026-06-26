package com.lira.infrastructure.reporttype

import com.lira.domain.reporttype.ReportType
import com.lira.domain.reporttype.ReportTypeRepository
import org.springframework.stereotype.Repository

@Repository
class JpaReportTypeRepositoryAdapter(
    private val jpaReportTypeRepository: JpaReportTypeRepository
) : ReportTypeRepository {

    override fun getAllReportTypes(): List<ReportType> =
        jpaReportTypeRepository.findAll().map { it.toDomain() }
}
