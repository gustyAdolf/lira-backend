package com.lira.infrastructure.therapistschedule

import com.lira.application.therapistschedule.CreateScheduleExceptions
import com.lira.application.therapistschedule.DeleteScheduleException
import com.lira.application.therapistschedule.GetScheduleExceptions
import com.lira.infrastructure.therapistschedule.dto.TherapistScheduleExceptionRequest
import com.lira.infrastructure.therapistschedule.dto.TherapistScheduleExceptionResponse
import com.lira.infrastructure.therapistschedule.dto.toDomainList
import com.lira.infrastructure.therapistschedule.dto.toDto
import com.lira.infrastructure.rest.ApiResponse
import com.lira.infrastructure.rest.ApiResponseStatus
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import java.time.LocalDate

@RestController
@RequestMapping("/therapist/{therapistId}/schedule/exceptions")
class TherapistScheduleExceptionController(
    private val getScheduleExceptions: GetScheduleExceptions,
    private val createScheduleExceptions: CreateScheduleExceptions,
    private val deleteScheduleException: DeleteScheduleException,
) {
    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMIN','COMPANY','THERAPIST')")
    fun getExceptions(@PathVariable therapistId: Int): ResponseEntity<List<TherapistScheduleExceptionResponse>> =
        ResponseEntity.ok(getScheduleExceptions.execute(therapistId).map { it.toDto() })

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ADMIN','COMPANY')")
    fun createExceptions(
        @PathVariable therapistId: Int,
        @RequestBody request: TherapistScheduleExceptionRequest,
    ): ResponseEntity<ApiResponse<List<TherapistScheduleExceptionResponse>>> {
        val exceptions = request.toDomainList(therapistId)
        createScheduleExceptions.execute(exceptions)
        return ResponseEntity.ok(
            ApiResponse(ApiResponseStatus.SUCCESS, "Excepción guardada", exceptions.map { it.toDto() })
        )
    }

    @DeleteMapping("/{date}")
    @PreAuthorize("hasAnyAuthority('ADMIN','COMPANY')")
    fun deleteException(
        @PathVariable therapistId: Int,
        @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) date: LocalDate,
    ): ResponseEntity<Void> {
        deleteScheduleException.execute(therapistId, date)
        return ResponseEntity.ok().build()
    }
}
