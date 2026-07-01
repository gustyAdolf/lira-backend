package com.lira.infrastructure.report

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface JpaReportDataRepository : JpaRepository<ReportEntity, Int> {
    fun findAllByTherapistId(therapistId: Int): List<ReportEntity>
}
