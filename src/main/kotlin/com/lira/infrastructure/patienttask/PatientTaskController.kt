package com.lira.infrastructure.patienttask

import com.lira.application.patienttask.CreatePatientTask
import com.lira.application.patienttask.DeletePatientTask
import com.lira.application.patienttask.GetPatientTasks
import com.lira.infrastructure.patienttask.dto.PatientTaskRequest
import com.lira.infrastructure.patienttask.dto.PatientTaskResponse
import com.lira.infrastructure.patienttask.dto.toResponse
import com.lira.infrastructure.rest.ApiResponse
import com.lira.infrastructure.rest.ApiResponseStatus
import com.lira.infrastructure.security.LiraUserDetails
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/patient-tasks")
class PatientTaskController(
    private val createPatientTask: CreatePatientTask,
    private val getPatientTasks: GetPatientTasks,
    private val deletePatientTask: DeletePatientTask,
) {

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ADMIN','PATIENT')")
    fun createTask(
        @RequestBody request: PatientTaskRequest,
        @AuthenticationPrincipal userDetails: LiraUserDetails,
    ): PatientTaskResponse {
        return createPatientTask.execute(request.copy(patientId = userDetails.user.id)).toResponse()
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("hasAnyAuthority('ADMIN','PATIENT')")
    fun getTasksByUser(
        @PathVariable userId: Int,
        @AuthenticationPrincipal userDetails: LiraUserDetails,
    ): List<PatientTaskResponse> {
        if (userDetails.user.id != userId) {
            throw AccessDeniedException("No puedes ver las tareas de otro paciente")
        }
        return getPatientTasks.execute(userId).map { it.toResponse() }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN','PATIENT')")
    fun deleteTask(
        @PathVariable id: Int,
        @AuthenticationPrincipal userDetails: LiraUserDetails,
    ): ApiResponse<Unit> {
        deletePatientTask.execute(id, userDetails.user.id)
        return ApiResponse(ApiResponseStatus.SUCCESS, "Tarea eliminada")
    }
}
