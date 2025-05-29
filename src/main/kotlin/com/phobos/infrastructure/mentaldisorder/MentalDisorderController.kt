package com.phobos.infrastructure.mentaldisorder

import com.phobos.application.mentaldisorder.GetMentalDisorders
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/mental-disorder")
class MentalDisorderController(
    private val getMentalDisorders: GetMentalDisorders
) {

    @GetMapping
    fun getMentalDisorder(): ResponseEntity<List<MentalDisorderResponse>> {
        val mentalDisorders = getMentalDisorders.execute()
        return ResponseEntity.ok(mentalDisorders.map { it.toResponse() })
    }
}