package com.lira.infrastructure.appointment

import com.lira.application.appointment.*
import com.lira.infrastructure.appointment.dto.AppointmentRequest
import com.lira.infrastructure.appointment.dto.AppointmentResponse
import com.lira.infrastructure.appointment.dto.CloseSessionRequest
import com.lira.infrastructure.appointment.dto.UpdateAppointmentRequest
import com.lira.infrastructure.appointment.dto.UpdateAppointmentStatusRequest
import com.lira.infrastructure.appointment.dto.toResponse
import com.lira.infrastructure.rest.ApiResponse
import com.lira.infrastructure.rest.ApiResponseStatus
import com.lira.infrastructure.security.LiraUserDetails
import com.lira.infrastructure.util.PageableUtil
import org.springframework.data.domain.PageRequest
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import java.time.LocalDate

@RestController
@RequestMapping("/appointment")
class AppointmentController(
    private val getTherapistAppointmentsByDateRanges: GetTherapistAppointmentsByDateRanges,
    private val getPatientAppointmentsByDateRanges: GetPatientAppointmentsByDateRanges,
    private val getCompanyAppointmentsByDateRanges: GetCompanyAppointmentsByDateRanges,
    private val getNextTherapistAppointments: GetNextTherapistAppointments,
    private val getNextPatientAppointments: GetNextPatientAppointments,
    private val createAppointment: CreateAppointment,
    private val updateAppointment: UpdateAppointment,
    private val updateAppointmentStatus: UpdateAppointmentStatus,
    private val deleteAppointment: DeleteAppointment,
    private val closeAppointmentSession: CloseAppointmentSession,
    private val getAppointmentsByPlan: GetAppointmentsByPlan,
    private val confirmAppointmentAttendance: ConfirmAppointmentAttendance,
    private val cancelAppointmentByPatient: CancelAppointmentByPatient,
    private val requestAppointmentCancellation: RequestAppointmentCancellation,
    private val getAppointmentById: GetAppointmentById
) {
    @GetMapping("/{appointmentId}")
    @PreAuthorize("hasAnyAuthority('ADMIN','THERAPIST','PATIENT','COMPANY')")
    fun getAppointmentById(@PathVariable appointmentId: Int): ResponseEntity<AppointmentResponse> =
        ResponseEntity.ok(getAppointmentById.execute(appointmentId).toResponse())

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMIN','THERAPIST')")
    fun getTherapistAppointments(
        @RequestParam(value = "therapistId") therapistId: Int,
        @RequestParam(value = "startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") startDate: LocalDate,
        @RequestParam(value = "endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") endDate: LocalDate,
        @RequestParam(value = "sortBy", defaultValue = "appointmentDate") sortBy: String,
        @RequestParam(value = "direction", defaultValue = "ASC") direction: String,
    ): ResponseEntity<List<AppointmentResponse>> {

        val appointments = getTherapistAppointmentsByDateRanges.execute(
            therapistId,
            startDate.atStartOfDay(),
            endDate.atTime(23, 59, 59),
            PageableUtil.getSort(direction, sortBy)
        )

        return ResponseEntity.ok(appointments.map { it.toResponse() })
    }

    @GetMapping("/patient")
    @PreAuthorize("hasAnyAuthority('ADMIN','PATIENT')")
    fun getPatientAppointments(
        @RequestParam(value = "patientId") patientId: Int,
        @RequestParam(value = "startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") startDate: LocalDate,
        @RequestParam(value = "endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") endDate: LocalDate,
        @RequestParam(value = "sortBy", defaultValue = "appointmentDate") sortBy: String,
        @RequestParam(value = "direction", defaultValue = "ASC") direction: String,
    ): ResponseEntity<List<AppointmentResponse>> {

        val appointments = getPatientAppointmentsByDateRanges.execute(
            patientId,
            startDate.atStartOfDay(),
            endDate.atTime(23, 59, 59),
            PageableUtil.getSort(direction, sortBy)
        )

        return ResponseEntity.ok(appointments.map { it.toResponse() })
    }

    @GetMapping("/company")
    @PreAuthorize("hasAnyAuthority('ADMIN','COMPANY','THERAPIST')")
    fun getCompanyAppointments(
        @RequestParam(value = "companyId") companyId: Int,
        @RequestParam(value = "startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") startDate: LocalDate,
        @RequestParam(value = "endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") endDate: LocalDate,
        @RequestParam(value = "sortBy", defaultValue = "appointmentDate") sortBy: String,
        @RequestParam(value = "direction", defaultValue = "ASC") direction: String,
    ): ResponseEntity<List<AppointmentResponse>> {

        val appointments = getCompanyAppointmentsByDateRanges.execute(
            companyId,
            startDate.atStartOfDay(),
            endDate.atTime(23, 59, 59),
            PageableUtil.getSort(direction, sortBy)
        )

        return ResponseEntity.ok(appointments.map { it.toResponse() })
    }

    @GetMapping("/patient/next")
    @PreAuthorize("hasAnyAuthority('ADMIN','PATIENT')")
    fun getNextPatientAppointments(
        @RequestParam(value = "patientId") patientId: Int,
        @RequestParam(value = "numOfAppointments", defaultValue = "10") numOfAppointments: Int,
    ): ResponseEntity<List<AppointmentResponse>> {

        val appointments = getNextPatientAppointments.execute(patientId, numOfAppointments)
        return ResponseEntity.ok(appointments.map { it.toResponse() })
    }

    @GetMapping("/next")
    @PreAuthorize("hasAnyAuthority('ADMIN','THERAPIST')")
    fun getNextTherapistAppointments(
        @RequestParam(value = "therapistId") therapistId: Int
    ): ResponseEntity<List<AppointmentResponse>> {

        val pageable = PageRequest.of(0, 10)
        val appointments = getNextTherapistAppointments.execute(therapistId, pageable)
        return ResponseEntity.ok(appointments.map { it.toResponse() })
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ADMIN','THERAPIST','COMPANY')")
    fun createAppointment(@RequestBody appointmentRequest: AppointmentRequest): ResponseEntity<AppointmentResponse> {

        val createdAppointment = createAppointment.execute(appointmentRequest)
        return ResponseEntity.ok(createdAppointment.toResponse())
    }

    @PutMapping("/{appointmentId}")
    @PreAuthorize("hasAnyAuthority('ADMIN','THERAPIST','COMPANY')")
    fun updateAppointment(
        @PathVariable appointmentId: Int,
        @RequestBody request: UpdateAppointmentRequest
    ): ResponseEntity<AppointmentResponse> {
        val updated = updateAppointment.execute(appointmentId, request)
        return ResponseEntity.ok(updated.toResponse())
    }

    @PatchMapping("/{appointmentId}/status")
    @PreAuthorize("hasAnyAuthority('ADMIN','THERAPIST','PATIENT','COMPANY')")
    fun updateAppointmentStatus(
        @PathVariable appointmentId: Int,
        @RequestBody appointmentStatusUpdateRequest: UpdateAppointmentStatusRequest
    ): ApiResponse<AppointmentResponse> {
        val response = updateAppointmentStatus.execute(appointmentId, appointmentStatusUpdateRequest).toResponse()
        return ApiResponse(ApiResponseStatus.SUCCESS, "Estado actualizado correctamente", response)
    }

    @PostMapping("/{appointmentId}/confirm")
    @PreAuthorize("hasAuthority('PATIENT')")
    fun confirmAttendance(
        @PathVariable appointmentId: Int,
        @AuthenticationPrincipal userDetails: LiraUserDetails
    ): ApiResponse<AppointmentResponse> {
        val response = confirmAppointmentAttendance.execute(appointmentId, userDetails.user.id).toResponse()
        return ApiResponse(ApiResponseStatus.SUCCESS, "Asistencia confirmada correctamente", response)
    }

    @PostMapping("/{appointmentId}/cancel")
    @PreAuthorize("hasAuthority('PATIENT')")
    fun cancelAppointment(
        @PathVariable appointmentId: Int,
        @AuthenticationPrincipal userDetails: LiraUserDetails
    ): ApiResponse<AppointmentResponse> {
        val response = cancelAppointmentByPatient.execute(appointmentId, userDetails.user.id).toResponse()
        return ApiResponse(ApiResponseStatus.SUCCESS, "Cita cancelada correctamente", response)
    }

    @PostMapping("/{appointmentId}/request-cancellation")
    @PreAuthorize("hasAuthority('PATIENT')")
    fun requestCancellation(
        @PathVariable appointmentId: Int,
        @AuthenticationPrincipal userDetails: LiraUserDetails
    ): ApiResponse<AppointmentResponse> {
        val response = requestAppointmentCancellation.execute(appointmentId, userDetails.user.id).toResponse()
        return ApiResponse(ApiResponseStatus.SUCCESS, "Solicitud de cancelación enviada", response)
    }

    @DeleteMapping("/{appointmentId}")
    @PreAuthorize("hasAnyAuthority('ADMIN','THERAPIST','COMPANY')")
    fun deleteAppointment(@PathVariable("appointmentId") appointmentId: Int): ResponseEntity<Void> {
        deleteAppointment.execute(appointmentId)
        return ResponseEntity.ok().build()
    }

    @PostMapping("/{appointmentId}/close-session")
    @PreAuthorize("hasAnyAuthority('ADMIN','THERAPIST')")
    fun closeSession(
        @PathVariable appointmentId: Int,
        @RequestBody request: CloseSessionRequest
    ): ResponseEntity<Unit> {
        closeAppointmentSession.execute(appointmentId, request.therapistNotes)
        return ResponseEntity.ok().build()
    }

    @GetMapping("/plan/{planId}")
    @PreAuthorize("hasAnyAuthority('ADMIN','THERAPIST')")
    fun getAppointmentsByPlan(@PathVariable planId: Int): ResponseEntity<List<AppointmentResponse>> =
        ResponseEntity.ok(getAppointmentsByPlan.execute(planId).map { it.toResponse() })

}