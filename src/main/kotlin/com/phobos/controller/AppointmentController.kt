package com.phobos.controller

import com.phobos.application.dto.appointment.AppointmentRequest
import com.phobos.application.dto.appointment.AppointmentResponse
import com.phobos.application.service.AppointmentService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Sort
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.LocalDate
import java.time.LocalDateTime

@RestController
@RequestMapping("/appointment")
class AppointmentController(
    @Autowired private val appointmentService: AppointmentService
) {
    @GetMapping
    fun getAppointments(
        @RequestParam(value = "therapistId") therapistId: Int,
        @RequestParam(value = "startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") startDate: LocalDate,
        @RequestParam(value = "endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") endDate: LocalDate,
        @RequestParam(value = "sortBy", defaultValue = "appointmentDate") sortBy: String,
        @RequestParam(value = "direction", defaultValue = "ASC") direction: String,
    ): ResponseEntity<List<AppointmentResponse>> {
        val startDateTime: LocalDateTime = startDate.atStartOfDay()
        val endDateTime: LocalDateTime = endDate.atTime(23, 59, 59)
        val sortDirection = if (direction.uppercase() == "DESC") Sort.Direction.DESC else Sort.Direction.ASC

        return ResponseEntity.ok(
            appointmentService.getAppointments(
                therapistId = therapistId,
                startDate = startDateTime,
                endDate = endDateTime,
                sortBy = sortBy,
                sortDirection = sortDirection
            )
        )
    }

    @PostMapping
    fun createAppointment(@RequestBody appointmentRequest: AppointmentRequest): ResponseEntity<AppointmentResponse> {
        val createdAppointment = appointmentService.createAppointment(appointmentRequest)
        return ResponseEntity.ok(createdAppointment)
    }

    @DeleteMapping("/{appointmentId}")
    fun deleteAppointment(@PathVariable("appointmentId") appointmentId: Int): ResponseEntity<Void> {
        appointmentService.deleteAppointment(appointmentId)
        return ResponseEntity.ok().build()
    }

}