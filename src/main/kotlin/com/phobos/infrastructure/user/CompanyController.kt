package com.phobos.infrastructure.user

import com.phobos.application.user.GetTherapistsAvailability
import com.phobos.infrastructure.user.dto.TherapistAvailabilityResponse
import com.phobos.infrastructure.user.dto.toResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/company")
class CompanyController(
    private val getTherapistsAvailability: GetTherapistsAvailability
) {

    @GetMapping("/{companyId}/therapists/availability")
    fun getTherapistsAvailability(@PathVariable companyId: String): ResponseEntity<List<TherapistAvailabilityResponse>> {
        val therapistAvailability = getTherapistsAvailability.execute(companyId.toInt())
        return ResponseEntity.ok(therapistAvailability.map { it.toResponse() })
    }
}