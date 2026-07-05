package com.lira.infrastructure.patienttask

import com.lira.application.patienttask.CreatePatientTaskEntry
import com.lira.application.patienttask.GetPatientJournalForTherapist
import com.lira.application.patienttask.GetPatientTaskEntries
import com.lira.infrastructure.patienttask.dto.PatientTaskEntryRequest
import com.lira.infrastructure.patienttask.dto.PatientTaskEntryResponse
import com.lira.infrastructure.patienttask.dto.PatientTaskJournalResponse
import com.lira.infrastructure.patienttask.dto.toResponse
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/patient-tasks/entries")
class PatientTaskEntryController(
    private val createPatientTaskEntry: CreatePatientTaskEntry,
    private val getPatientTaskEntries: GetPatientTaskEntries,
    private val getPatientJournalForTherapist: GetPatientJournalForTherapist,
) {

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ADMIN','THERAPIST','PATIENT')")
    fun createEntry(@RequestBody request: PatientTaskEntryRequest): PatientTaskEntryResponse {
        return createPatientTaskEntry.execute(request).toResponse()
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("hasAnyAuthority('ADMIN','THERAPIST','PATIENT')")
    fun getEntriesByUser(@PathVariable userId: Int): List<PatientTaskEntryResponse> {
        return getPatientTaskEntries.execute(userId).map { it.toResponse() }
    }

    @GetMapping("/patient/{patientId}")
    @PreAuthorize("hasAnyAuthority('ADMIN','THERAPIST')")
    fun getJournalForPatient(@PathVariable patientId: Int): List<PatientTaskJournalResponse> {
        return getPatientJournalForTherapist.execute(patientId).map { it.toResponse() }
    }
}
