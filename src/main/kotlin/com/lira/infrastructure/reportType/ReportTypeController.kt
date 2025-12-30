package com.lira.infrastructure.reportType

import com.lira.application.mentaldisorder.GetMentalDisorders
import com.lira.application.reportType.GetReportTypes
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/report-type")
class ReportTypeController(
    private val getReportTypes: GetReportTypes
) {

    @GetMapping
    fun getReportType(): ResponseEntity<List<ReportTypeResponse>> {
        val reportTypes = getReportTypes.execute()
        return ResponseEntity.ok(reportTypes.map { it.toResponse() })
    }
}