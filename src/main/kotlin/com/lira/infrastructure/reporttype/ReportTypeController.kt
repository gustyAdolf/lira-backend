package com.lira.infrastructure.reporttype

import com.lira.application.reporttype.GetReportTypes
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/report-type")
class ReportTypeController(
    private val getReportTypes: GetReportTypes
) {

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMIN','COMPANY','THERAPIST')")
    fun getReportTypes(): ResponseEntity<List<ReportTypeResponse>> =
        ResponseEntity.ok(getReportTypes.execute().map { it.toResponse() })
}
