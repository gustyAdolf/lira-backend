package com.lira.application.report

import com.lira.domain.report.ReportTypeEnum

interface ReportDataValidator {
    fun supports(): ReportTypeEnum
    fun validate(data: Map<String, Any>)
}