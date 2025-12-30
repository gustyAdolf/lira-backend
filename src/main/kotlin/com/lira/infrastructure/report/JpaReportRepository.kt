package com.lira.infrastructure.report

import org.springframework.data.jpa.repository.JpaRepository

interface JpaReportRepository : JpaRepository<ReportEntity, Int> {

}