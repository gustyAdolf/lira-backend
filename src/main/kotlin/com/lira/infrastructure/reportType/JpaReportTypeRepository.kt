package com.lira.infrastructure.reportType

import org.springframework.data.jpa.repository.JpaRepository

interface JpaReportTypeRepository : JpaRepository<ReportTypeEntity, Int> {
}