package com.lira.infrastructure.reporttype

import org.springframework.data.jpa.repository.JpaRepository

interface JpaReportTypeRepository : JpaRepository<ReportTypeEntity, Int>
