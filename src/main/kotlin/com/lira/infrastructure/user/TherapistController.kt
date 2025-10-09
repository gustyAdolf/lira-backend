package com.lira.infrastructure.user

import com.lira.application.user.ListTherapistByCompany
import com.lira.domain.user.Therapist
import com.lira.infrastructure.user.dto.PatientResponse
import org.springframework.http.ResponseEntity
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
    fun getTherapistsByCompany(@PathVariable companyId: Int): ResponseEntity<List<PatientResponse>> {
        val therapists: List<Therapist> = listTherapistByCompany.execute(companyId)
        return TODO()
//        val response = therapists.map { it.toResponse() }
//        return ResponseEntity.ok(response)
    }
}