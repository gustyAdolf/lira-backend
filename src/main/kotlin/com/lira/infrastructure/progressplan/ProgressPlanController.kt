package com.lira.infrastructure.progressplan

import com.lira.application.progressplan.CreateProgressPlan
import com.lira.application.progressplan.GetProgressPlanByPatientId
import com.lira.application.progressplan.GetProgressPlansForPatient
import com.lira.application.progressplan.RegisterProgress
import com.lira.infrastructure.progressplan.dto.ProgressPlanRequest
import com.lira.infrastructure.progressplan.dto.ProgressPlanResponse
import com.lira.infrastructure.progressplan.dto.SubobjectiveEntryRequest
import com.lira.infrastructure.progressplan.dto.toResponse
import com.lira.infrastructure.rest.ApiResponse
import com.lira.infrastructure.rest.ApiResponseStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/progress-plans")
class ProgressPlanController(
    private val getProgressPlanByPatientId: GetProgressPlanByPatientId,
    private val getProgressPlansForPatient: GetProgressPlansForPatient,
    private val createProgressPlan: CreateProgressPlan,
    private val registerProgress: RegisterProgress
) {

    @GetMapping("/patient/{patientId}")
    @PreAuthorize("hasAnyAuthority('ADMIN','THERAPIST','PATIENT')")
    fun getProgressPlanByPatient(
        @PathVariable patientId: Int,
        @RequestParam(value = "therapistId") therapistId: Int
    ): ResponseEntity<List<ProgressPlanResponse>> {
        return ResponseEntity.ok(getProgressPlanByPatientId.execute(patientId, therapistId))
    }

    @GetMapping("/patient/{patientId}/own")
    @PreAuthorize("hasAnyAuthority('ADMIN','THERAPIST','PATIENT')")
    fun getOwnPlans(
        @PathVariable patientId: Int
    ): ResponseEntity<List<ProgressPlanResponse>> {
        return ResponseEntity.ok(getProgressPlansForPatient.execute(patientId))
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ADMIN','THERAPIST')")
    fun createPlan(@RequestBody progressPlanRequest: ProgressPlanRequest): ApiResponse<Unit> {
        return try {
            createProgressPlan.execute(progressPlanRequest)
            return ApiResponse(ApiResponseStatus.SUCCESS, "Plan creado")
        } catch (e: Exception) {
            ApiResponse(ApiResponseStatus.ERROR, "Erro: ${e.message}")
        }
    }

    @PostMapping("subobjective/{subId}/entries")
    @PreAuthorize("hasAnyAuthority('ADMIN','THERAPIST')")
    fun addEntry(
        @PathVariable subId: Int,
        @RequestBody request: SubobjectiveEntryRequest
    ): ResponseEntity<ProgressPlanResponse> {
        val updatedPlan = registerProgress.execute(request)
        return ResponseEntity.ok(updatedPlan.toResponse())
    }

}