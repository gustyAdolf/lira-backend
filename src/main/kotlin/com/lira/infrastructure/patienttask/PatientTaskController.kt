package com.lira.infrastructure.patienttask

import com.lira.application.patienttask.CreatePatientTask
import com.lira.application.patienttask.DeletePatientTask
import com.lira.application.patienttask.GetPatientTasks
import com.lira.infrastructure.patienttask.dto.PatientTaskRequest
import com.lira.infrastructure.patienttask.dto.PatientTaskResponse
import com.lira.infrastructure.patienttask.dto.toResponse
import com.lira.infrastructure.rest.ApiResponse
import com.lira.infrastructure.rest.ApiResponseStatus
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/patient-tasks")
class PatientTaskController(
    private val createPatientTask: CreatePatientTask,
    private val getPatientTasks: GetPatientTasks,
    private val deletePatientTask: DeletePatientTask,
) {

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ADMIN','THERAPIST','PATIENT')")
    fun createTask(@RequestBody request: PatientTaskRequest): PatientTaskResponse {
        return createPatientTask.execute(request).toResponse()
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("hasAnyAuthority('ADMIN','THERAPIST','PATIENT')")
    fun getTasksByUser(@PathVariable userId: Int): List<PatientTaskResponse> {
        return getPatientTasks.execute(userId).map { it.toResponse() }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN','THERAPIST','PATIENT')")
    fun deleteTask(@PathVariable id: Int): ApiResponse<Unit> {
        deletePatientTask.execute(id)
        return ApiResponse(ApiResponseStatus.SUCCESS, "Tarea eliminada")
    }
}
