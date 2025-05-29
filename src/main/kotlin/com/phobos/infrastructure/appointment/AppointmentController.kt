package com.phobos.infrastructure.appointment

import com.phobos.application.appointment.CreateAppointment
import com.phobos.application.appointment.DeleteAppointment
import com.phobos.application.appointment.GetAppointmentsByDateRanges
import com.phobos.application.appointment.GetNextAppointments
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.LocalDate

@RestController
@RequestMapping("/appointment")
class AppointmentController(
    private val getAppointmentsByDateRanges: GetAppointmentsByDateRanges,
    private val getNextAppointments: GetNextAppointments,
    private val createAppointment: CreateAppointment,
    private val deleteAppointment: DeleteAppointment
) {
    @GetMapping
    fun getAppointments(
        @RequestParam(value = "therapistId") therapistId: Int,
        @RequestParam(value = "startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") startDate: LocalDate,
        @RequestParam(value = "endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") endDate: LocalDate,
        @RequestParam(value = "sortBy", defaultValue = "appointmentDate") sortBy: String,
        @RequestParam(value = "direction", defaultValue = "ASC") direction: String,
    ): ResponseEntity<List<AppointmentResponse>> {

        val sortDirection = if (direction.uppercase() == "DESC") Sort.Direction.DESC else Sort.Direction.ASC
        val sort = Sort.by(sortDirection, sortBy)
        val appointments = getAppointmentsByDateRanges.execute(
            therapistId,
            startDate.atStartOfDay(),
            endDate.atTime(23, 59, 59),
            sort
        )

        return ResponseEntity.ok(appointments.map { it.toResponse() })
    }

    @GetMapping("/next")
    fun getNextAppointments(
        @RequestParam(value = "therapistId") therapistId: Int
    ): ResponseEntity<List<AppointmentResponse>> {

        val pageable = PageRequest.of(0, 10)
        val appointments = getNextAppointments.execute(therapistId, pageable)
        return ResponseEntity.ok(appointments.map { it.toResponse() })
    }


    @PostMapping
    fun createAppointment(@RequestBody appointmentRequest: AppointmentRequest): ResponseEntity<AppointmentResponse> {

        val createdAppointment = createAppointment.execute(appointmentRequest)
        return ResponseEntity.ok(createdAppointment.toResponse())
    }

    @DeleteMapping("/{appointmentId}")
    fun deleteAppointment(@PathVariable("appointmentId") appointmentId: Int): ResponseEntity<Void> {

        deleteAppointment.execute(appointmentId)
        return ResponseEntity.ok().build()
    }

}