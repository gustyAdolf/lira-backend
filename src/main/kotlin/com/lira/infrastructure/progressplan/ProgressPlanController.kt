package com.lira.infrastructure.progressplan

import com.lira.application.progressplan.AddObjectiveToPlan
import com.lira.application.progressplan.AddSubobjectiveToPlan
import com.lira.application.progressplan.CreateProgressPlan
import com.lira.application.progressplan.DeleteObjective
import com.lira.application.progressplan.DeleteSubobjective
import com.lira.application.progressplan.GetProgressPlanByPatientId
import com.lira.application.progressplan.GetProgressPlansForPatient
import com.lira.application.progressplan.GetSubobjectiveEntries
import com.lira.application.progressplan.RegisterProgress
import com.lira.application.progressplan.UpdateObjective
import com.lira.application.progressplan.UpdateSubobjective
import com.lira.infrastructure.progressplan.dto.ProgressPlanRequest
import com.lira.infrastructure.progressplan.dto.ProgressPlanResponse
import com.lira.infrastructure.progressplan.dto.SubobjectiveEntryRequest
import com.lira.infrastructure.progressplan.dto.SubobjectiveEntryResponse
import com.lira.infrastructure.progressplan.dto.ObjectiveRequest
import com.lira.infrastructure.progressplan.dto.SubobjectiveRequest
import com.lira.infrastructure.progressplan.dto.UpdateObjectiveRequest
import com.lira.infrastructure.progressplan.dto.UpdateSubobjectiveRequest
import com.lira.infrastructure.progressplan.dto.toDomain
import com.lira.infrastructure.progressplan.dto.toEntryResponse
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
    private val registerProgress: RegisterProgress,
    private val getSubobjectiveEntries: GetSubobjectiveEntries,
    private val updateObjective: UpdateObjective,
    private val updateSubobjective: UpdateSubobjective,
    private val deleteObjective: DeleteObjective,
    private val deleteSubobjective: DeleteSubobjective,
    private val addObjectiveToPlan: AddObjectiveToPlan,
    private val addSubobjectiveToPlan: AddSubobjectiveToPlan
) {

    @GetMapping("/patient/{patientId}")
    @PreAuthorize("hasAnyAuthority('ADMIN','THERAPIST','PATIENT','COMPANY')")
    fun getProgressPlanByPatient(
        @PathVariable patientId: Int,
        @RequestParam(value = "therapistId", required = false) therapistId: Int?
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

    @GetMapping("subobjective/{subId}/entries")
    @PreAuthorize("hasAnyAuthority('ADMIN','THERAPIST')")
    fun getEntries(
        @PathVariable subId: Int,
        @RequestParam(defaultValue = "3") size: Int
    ): ResponseEntity<List<SubobjectiveEntryResponse>> {
        val entries = getSubobjectiveEntries.execute(subId, size)
            .map { it.toEntryResponse() }
        return ResponseEntity.ok(entries)
    }

    @PutMapping("objective/{objectiveId}")
    @PreAuthorize("hasAnyAuthority('ADMIN','THERAPIST')")
    fun updateObjective(
        @PathVariable objectiveId: Int,
        @RequestBody request: UpdateObjectiveRequest
    ): ResponseEntity<ProgressPlanResponse> =
        ResponseEntity.ok(updateObjective.execute(objectiveId, request.title, request.description))

    @PutMapping("subobjective/{subId}")
    @PreAuthorize("hasAnyAuthority('ADMIN','THERAPIST')")
    fun updateSubobjective(
        @PathVariable subId: Int,
        @RequestBody request: UpdateSubobjectiveRequest
    ): ResponseEntity<ProgressPlanResponse> =
        ResponseEntity.ok(updateSubobjective.execute(
            subId, request.objectiveId, request.title, request.description,
            request.targetValue, request.targetSuccess, request.targetFail
        ))

    @DeleteMapping("objective/{objectiveId}")
    @PreAuthorize("hasAnyAuthority('ADMIN','THERAPIST')")
    fun deleteObjective(@PathVariable objectiveId: Int): ResponseEntity<ProgressPlanResponse> =
        ResponseEntity.ok(deleteObjective.execute(objectiveId))

    @DeleteMapping("subobjective/{subId}")
    @PreAuthorize("hasAnyAuthority('ADMIN','THERAPIST')")
    fun deleteSubobjective(@PathVariable subId: Int): ResponseEntity<ProgressPlanResponse> =
        ResponseEntity.ok(deleteSubobjective.execute(subId))

    @PostMapping("{planId}/objective")
    @PreAuthorize("hasAnyAuthority('ADMIN','THERAPIST')")
    fun addObjective(
        @PathVariable planId: Int,
        @RequestBody request: ObjectiveRequest
    ): ResponseEntity<ProgressPlanResponse> =
        ResponseEntity.ok(addObjectiveToPlan.execute(planId, request.title, request.description))

    @PostMapping("objective/{objectiveId}/subobjective")
    @PreAuthorize("hasAnyAuthority('ADMIN','THERAPIST')")
    fun addSubobjective(
        @PathVariable objectiveId: Int,
        @RequestBody request: SubobjectiveRequest
    ): ResponseEntity<ProgressPlanResponse> =
        ResponseEntity.ok(addSubobjectiveToPlan.execute(objectiveId, request.toDomain()))
}
