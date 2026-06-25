package com.lira.infrastructure.user

import com.lira.application.user.GetTherapistPatients
import com.lira.application.user.ListTherapistByCompany
import com.lira.infrastructure.user.dto.PatientWithRelationResponse
import com.lira.infrastructure.user.dto.TherapistResponse
import com.lira.infrastructure.user.dto.toResponse
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/therapist")
class TherapistController(
    private val listTherapistByCompany: ListTherapistByCompany,
    private val getTherapistPatients: GetTherapistPatients
) {
    @GetMapping("/company/{companyId}")
    @PreAuthorize("hasAnyAuthority('ADMIN','COMPANY')")
    fun getTherapistsByCompany(@PathVariable companyId: Int): ResponseEntity<List<TherapistResponse>> {
        val therapists = listTherapistByCompany.execute(companyId).map { it.toResponse() }
        return ResponseEntity.ok(therapists)
    }

    @GetMapping("/{therapistId}/patients")
    @PreAuthorize("hasAnyAuthority('ADMIN','THERAPIST')")
    fun getTherapistPatients(
        @PathVariable therapistId: Int,
        @RequestParam(required = false) name: String?,
        @RequestParam(defaultValue = "20") size: Int
    ): ResponseEntity<List<PatientWithRelationResponse>> {
        val result = getTherapistPatients.execute(therapistId, name, size)
        return ResponseEntity.ok(result.map { it.toResponse() })
    }
}
