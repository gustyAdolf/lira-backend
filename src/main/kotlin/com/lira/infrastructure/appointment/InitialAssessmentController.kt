package com.lira.infrastructure.appointment

import com.lira.application.appointment.GetAllPatientAssessments
import com.lira.application.appointment.GetInitialAssessment
import com.lira.application.appointment.GetPatientInitialAssessment
import com.lira.application.appointment.SaveInitialAssessment
import com.lira.infrastructure.appointment.dto.InitialAssessmentRequest
import com.lira.infrastructure.appointment.dto.InitialAssessmentResponse
import com.lira.infrastructure.appointment.dto.toPatch
import com.lira.infrastructure.appointment.dto.toResponse
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RestController
class InitialAssessmentController(
    private val saveInitialAssessment: SaveInitialAssessment,
    private val getInitialAssessment: GetInitialAssessment,
    private val getPatientInitialAssessment: GetPatientInitialAssessment,
    private val getAllPatientAssessments: GetAllPatientAssessments,
) {
    @PostMapping("/appointment/{appointmentId}/initial-assessment")
    @PreAuthorize("hasAnyAuthority('ADMIN','THERAPIST')")
    fun saveAssessment(
        @PathVariable appointmentId: Int,
        @RequestBody request: InitialAssessmentRequest,
    ): ResponseEntity<InitialAssessmentResponse> {
        val result = saveInitialAssessment.execute(appointmentId, request.toPatch())
        return ResponseEntity.ok(result.toResponse())
    }

    @PatchMapping("/appointment/{appointmentId}/initial-assessment")
    @PreAuthorize("hasAnyAuthority('ADMIN','THERAPIST')")
    fun patchAssessment(
        @PathVariable appointmentId: Int,
        @RequestBody request: InitialAssessmentRequest,
    ): ResponseEntity<InitialAssessmentResponse> {
        val result = saveInitialAssessment.execute(appointmentId, request.toPatch())
        return ResponseEntity.ok(result.toResponse())
    }

    @GetMapping("/appointment/{appointmentId}/initial-assessment")
    @PreAuthorize("hasAnyAuthority('ADMIN','THERAPIST')")
    fun getAssessment(
        @PathVariable appointmentId: Int,
    ): ResponseEntity<InitialAssessmentResponse> {
        val assessment = getInitialAssessment.execute(appointmentId)
            ?: return ResponseEntity.notFound().build()
        return ResponseEntity.ok(assessment.toResponse())
    }

    @GetMapping("/patients/{patientId}/initial-assessment")
    @PreAuthorize("hasAnyAuthority('ADMIN','THERAPIST')")
    fun getPatientAssessment(
        @PathVariable patientId: Int,
    ): ResponseEntity<InitialAssessmentResponse> {
        val assessment = getPatientInitialAssessment.execute(patientId)
            ?: return ResponseEntity.notFound().build()
        return ResponseEntity.ok(assessment.toResponse())
    }

    @GetMapping("/patients/{patientId}/assessments")
    @PreAuthorize("hasAnyAuthority('ADMIN','THERAPIST')")
    fun getAllAssessments(
        @PathVariable patientId: Int,
    ): ResponseEntity<List<InitialAssessmentResponse>> {
        val assessments = getAllPatientAssessments.execute(patientId)
        return ResponseEntity.ok(assessments.map { it.toResponse() })
    }
}
