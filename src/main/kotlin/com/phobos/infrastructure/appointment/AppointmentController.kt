package com.phobos.infrastructure.appointment

import com.phobos.application.appointment.*
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.http.ResponseEntity
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
    private val deleteAppointment: DeleteAppointment
) {
    @GetMapping
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
            getSort(direction, sortBy)
        )

        return ResponseEntity.ok(appointments.map { it.toResponse() })
    }

    @GetMapping("/patient")
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
            getSort(direction, sortBy)
        )

        return ResponseEntity.ok(appointments.map { it.toResponse() })
    }

    @GetMapping("/company")
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
            getSort(direction, sortBy)
        )

        return ResponseEntity.ok(appointments.map { it.toResponse() })
    }

    @GetMapping("/patient/next")
    fun getNextPatientAppointments(
        @RequestParam(value = "patientId") patientId: Int,
        @RequestParam(value = "numOfAppointments", defaultValue = "10") numOfAppointments: Int,
    ): ResponseEntity<List<AppointmentResponse>> {

        val appointments = getNextPatientAppointments.execute(patientId, numOfAppointments)
        return ResponseEntity.ok(appointments.map { it.toResponse() })
    }

    @GetMapping("/next")
    fun getNextTherapistAppointments(
        @RequestParam(value = "therapistId") therapistId: Int
    ): ResponseEntity<List<AppointmentResponse>> {

        val pageable = PageRequest.of(0, 10)
        val appointments = getNextTherapistAppointments.execute(therapistId, pageable)
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

    private fun getSort(direction: String, sortBy: String): Sort {
        val sortDirection = if (direction.uppercase() == "DESC") Sort.Direction.DESC else Sort.Direction.ASC
        return Sort.by(sortDirection, sortBy)
    }

}