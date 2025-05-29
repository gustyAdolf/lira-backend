package com.phobos.infrastructure.user

import com.phobos.application.user.ListTherapistByCompany
import com.phobos.domain.user.User
import com.phobos.infrastructure.user.dto.UserResponse
import com.phobos.infrastructure.user.dto.toResponse
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
    fun getTherapistsByCompany(@PathVariable companyId: Int): ResponseEntity<List<UserResponse>> {
        val therapists: List<User> = listTherapistByCompany.execute(companyId)
        val response = therapists.map { it.toResponse() }
        return ResponseEntity.ok(response)
    }
}