package com.lira.infrastructure.report

import com.lira.application.report.CreateReport
import com.lira.infrastructure.report.dto.ReportRequest
import com.lira.infrastructure.report.dto.ReportResponse
import com.lira.infrastructure.report.dto.toResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/report")
class ReportController(
    private val createReport: CreateReport
){

    @PostMapping
    fun createReport(@RequestBody reportRequest: ReportRequest): ResponseEntity<ReportResponse> {

        val createdReport = createReport.execute(reportRequest)
        return ResponseEntity.ok(createdReport.toResponse())
    }

}