package com.lira.infrastructure.mentaldisorder

import com.lira.application.mentaldisorder.GetMentalDisorders
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/mental-disorder")
class MentalDisorderController(
    private val getMentalDisorders: GetMentalDisorders
) {

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    fun getMentalDisorder(): ResponseEntity<List<MentalDisorderResponse>> {
        val mentalDisorders = getMentalDisorders.execute()
        return ResponseEntity.ok(mentalDisorders.map { it.toResponse() })
    }
}