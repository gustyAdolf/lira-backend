package com.lira.infrastructure.user

import com.lira.application.user.ListTherapistByCompany
import com.lira.infrastructure.user.dto.TherapistResponse
import com.lira.infrastructure.user.dto.toResponse
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/therapist")
class TherapistController(
    private val listTherapistByCompany: ListTherapistByCompany
) {
    @GetMapping("/company/{companyId}")
    @PreAuthorize("hasAnyAuthority('ADMIN','COMPANY')")
    fun getTherapistsByCompany(@PathVariable companyId: Int): ResponseEntity<List<TherapistResponse>> {
        val therapists = listTherapistByCompany.execute(companyId).map { it.toResponse() }
        return ResponseEntity.ok(therapists)
    }
}
