package com.lira.infrastructure.report

import com.lira.application.report.CreateReport
import com.lira.application.report.GenerateReportPdf
import com.lira.application.report.GetReportsByTherapist
import com.lira.infrastructure.report.dto.ReportRequest
import com.lira.infrastructure.report.dto.ReportResponse
import com.lira.infrastructure.report.dto.toResponse
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/report")
class ReportController(
    private val createReport: CreateReport,
    private val getReportsByTherapist: GetReportsByTherapist,
    private val generateReportPdf: GenerateReportPdf
){

    @PostMapping
    fun createReport(@RequestBody reportRequest: ReportRequest): ResponseEntity<ReportResponse> {

        val createdReport = createReport.execute(reportRequest)
        return ResponseEntity.ok(createdReport.toResponse())
    }

    @GetMapping("/therapist/{therapistId}")
    fun getByTherapist(@PathVariable therapistId: Long): ResponseEntity<List<ReportResponse>> {
        val reports = getReportsByTherapist.execute(therapistId)

        // Mapeamos de Dominio a DTO de respuesta para no exponer la entidad pura
        return ResponseEntity.ok(reports.map { it.toResponse() })
    }

    @GetMapping("/{id}/pdf", produces = [MediaType.APPLICATION_PDF_VALUE])
    fun downloadPdf(@PathVariable id: Long): ResponseEntity<ByteArray> {
        val bytes = generateReportPdf.execute(id)
        val headers = HttpHeaders().apply {
            contentType = MediaType.APPLICATION_PDF
            setContentDispositionFormData("inline", "report_$id.pdf")
        }
        return ResponseEntity(bytes, headers, HttpStatus.OK)
    }

}