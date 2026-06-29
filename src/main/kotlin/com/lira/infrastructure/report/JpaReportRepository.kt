package com.lira.infrastructure.report

import com.lira.domain.report.Report
import org.springframework.data.jpa.repository.JpaRepository

interface JpaReportRepository : JpaRepository<ReportEntity, Int> {

    fun findByTherapistIdOrderByCreatedAtDesc(therapistId: Long): List<ReportEntity>

}