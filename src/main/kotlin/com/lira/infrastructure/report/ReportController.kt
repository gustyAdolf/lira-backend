package com.lira.infrastructure.report

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.lira.application.report.CreateReport
import com.lira.application.report.GetReport
import com.lira.application.report.GetReportsByTherapist
import com.lira.domain.report.Report
import com.lira.infrastructure.report.dto.ReportRequest
import com.lira.infrastructure.report.dto.ReportResponse
import com.lira.infrastructure.report.dto.ReportDetailResponse
import com.lira.infrastructure.report.dto.toResponse
import com.lira.infrastructure.report.pdf.ReportPdfService
import com.lira.infrastructure.reporttype.JpaReportTypeRepository
import com.lira.infrastructure.user.jpa.JpaUserRepository
import org.slf4j.LoggerFactory
import org.springframework.http.*
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import java.time.LocalDate

@RestController
@RequestMapping("/reports")
class ReportController(
    private val createReport: CreateReport,
    private val getReport: GetReport,
    private val getReportsByTherapist: GetReportsByTherapist,
    private val reportPdfService: ReportPdfService,
    private val jpaReportDataRepository: JpaReportDataRepository,
    private val jpaUserRepository: JpaUserRepository,
    private val jpaReportTypeRepository: JpaReportTypeRepository,
    private val objectMapper: ObjectMapper
) {
    private val log = LoggerFactory.getLogger(ReportController::class.java)

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ADMIN','THERAPIST')")
    fun create(@RequestBody request: ReportRequest): ResponseEntity<ReportResponse> {
        val report = Report(
            id = null,
            therapistId = request.therapistId,
            userId = request.userId,
            reportTypeId = request.reportTypeId,
            dataReport = objectMapper.writeValueAsString(request.dataReport),
            createdAt = LocalDate.now()
        )
        return ResponseEntity.status(HttpStatus.CREATED).body(createReport.execute(report).toResponse())
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMIN','THERAPIST')")
    fun listByTherapist(@RequestParam therapistId: Int): ResponseEntity<List<ReportResponse>> {
        val reports = getReportsByTherapist.execute(therapistId)
        val usersById = jpaUserRepository.findAllById(reports.map { it.userId }.distinct())
            .associateBy { it.id }
        val typesById = jpaReportTypeRepository.findAllById(reports.map { it.reportTypeId }.distinct())
            .associateBy { it.id }
        return ResponseEntity.ok(reports.map { report ->
            report.toResponse(
                patientName = usersById[report.userId]?.name ?: "",
                reportTypeName = typesById[report.reportTypeId]?.name ?: ""
            )
        })
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN','THERAPIST')")
    fun getById(@PathVariable id: Int): ResponseEntity<ReportDetailResponse> {
        val entity = jpaReportDataRepository.findById(id).orElseThrow {
            NoSuchElementException("Report $id not found")
        }
        val patient = jpaUserRepository.findById(entity.userId).orElse(null)
        val type = jpaReportTypeRepository.findById(entity.reportTypeId).orElse(null)
        val dataMap = objectMapper.readValue<Map<String, Any?>>(entity.dataReport)
        return ResponseEntity.ok(ReportDetailResponse(
            id = entity.id,
            therapistId = entity.therapistId,
            userId = entity.userId,
            patientName = patient?.name ?: "",
            reportTypeId = entity.reportTypeId,
            reportTypeName = type?.name ?: "",
            createdAt = entity.createdAt.toString(),
            dataReport = dataMap
        ))
    }

    @GetMapping("/{id}/pdf", produces = [MediaType.APPLICATION_PDF_VALUE])
    @PreAuthorize("hasAnyAuthority('ADMIN','THERAPIST')")
    fun downloadPdf(@PathVariable id: Int): ResponseEntity<ByteArray> {
        val entity = jpaReportDataRepository.findById(id).orElseThrow {
            NoSuchElementException("Report $id not found")
        }
        return try {
            val bytes = reportPdfService.generate(entity)
            val headers = HttpHeaders().apply {
                contentType = MediaType.APPLICATION_PDF
                setContentDispositionFormData("inline", "informe_$id.pdf")
            }
            ResponseEntity(bytes, headers, HttpStatus.OK)
        } catch (e: Exception) {
            log.error("PDF generation failed for report $id: ${e.message}", e)
            throw e
        }
    }
}
