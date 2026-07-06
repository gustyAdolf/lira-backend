package com.lira.infrastructure.patienttask

import com.lira.application.patienttask.CreatePatientTaskEntry
import com.lira.application.patienttask.GetPatientJournalForTherapist
import com.lira.application.patienttask.GetPatientTaskEntries
import com.lira.infrastructure.patienttask.dto.PatientTaskEntryRequest
import com.lira.infrastructure.patienttask.dto.PatientTaskEntryResponse
import com.lira.infrastructure.patienttask.dto.PatientTaskJournalResponse
import com.lira.infrastructure.patienttask.dto.toResponse
import com.lira.infrastructure.security.LiraUserDetails
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/patient-tasks/entries")
class PatientTaskEntryController(
    private val createPatientTaskEntry: CreatePatientTaskEntry,
    private val getPatientTaskEntries: GetPatientTaskEntries,
    private val getPatientJournalForTherapist: GetPatientJournalForTherapist,
) {

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ADMIN','PATIENT')")
    fun createEntry(
        @RequestBody request: PatientTaskEntryRequest,
        @AuthenticationPrincipal userDetails: LiraUserDetails,
    ): PatientTaskEntryResponse {
        return createPatientTaskEntry.execute(request.copy(patientId = userDetails.user.id)).toResponse()
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("hasAnyAuthority('ADMIN','PATIENT')")
    fun getEntriesByUser(
        @PathVariable userId: Int,
        @AuthenticationPrincipal userDetails: LiraUserDetails,
    ): List<PatientTaskEntryResponse> {
        if (userDetails.user.id != userId) {
            throw AccessDeniedException("No puedes ver las entradas de otro paciente")
        }
        return getPatientTaskEntries.execute(userId).map { it.toResponse() }
    }

    @GetMapping("/patient/{patientId}")
    @PreAuthorize("hasAnyAuthority('ADMIN','THERAPIST')")
    fun getJournalForPatient(
        @PathVariable patientId: Int,
        @AuthenticationPrincipal userDetails: LiraUserDetails,
    ): List<PatientTaskJournalResponse> {
        return getPatientJournalForTherapist.execute(patientId, userDetails.user.id).map { it.toResponse() }
    }
}
