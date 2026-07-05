package com.lira.infrastructure.therapistschedule

import com.lira.application.therapistschedule.TherapistScheduleService
import com.lira.infrastructure.therapistschedule.dto.TherapistScheduleDayDto
import com.lira.infrastructure.therapistschedule.dto.toDomain
import com.lira.infrastructure.therapistschedule.dto.toDto
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import java.time.LocalDate
import java.time.LocalTime

@RestController
@RequestMapping("/therapist/{therapistId}/schedule")
class TherapistScheduleController(
    private val scheduleService: TherapistScheduleService,
) {
    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMIN','COMPANY','THERAPIST')")
    fun getSchedule(@PathVariable therapistId: Int): ResponseEntity<List<TherapistScheduleDayDto>> =
        ResponseEntity.ok(scheduleService.getSchedule(therapistId).map { it.toDto() })

    @PutMapping
    @PreAuthorize("hasAnyAuthority('ADMIN','COMPANY')")
    fun replaceSchedule(
        @PathVariable therapistId: Int,
        @RequestBody days: List<TherapistScheduleDayDto>,
    ): ResponseEntity<List<TherapistScheduleDayDto>> {
        val domainDays = days.map { it.toDomain(therapistId) }
        scheduleService.replaceSchedule(therapistId, domainDays)
        return ResponseEntity.ok(domainDays.map { it.toDto() })
    }

    @GetMapping("/slots")
    @PreAuthorize("hasAnyAuthority('ADMIN','COMPANY','THERAPIST')")
    fun getAvailableSlots(
        @PathVariable therapistId: Int,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) date: LocalDate,
        @RequestParam duration: Int,
    ): ResponseEntity<List<LocalTime>> =
        ResponseEntity.ok(scheduleService.getAvailableSlots(therapistId, date, duration))
}
