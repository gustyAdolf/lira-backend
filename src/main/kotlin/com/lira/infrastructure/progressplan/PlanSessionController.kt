package com.lira.infrastructure.progressplan

import com.lira.application.progressplan.CreatePlanSession
import com.lira.application.progressplan.GetPlanSessionsByPlan
import com.lira.infrastructure.progressplan.dto.PlanSessionRequest
import com.lira.infrastructure.progressplan.dto.PlanSessionResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/plan-sessions")
class PlanSessionController(
    private val createPlanSession: CreatePlanSession,
    private val getPlanSessionsByPlan: GetPlanSessionsByPlan
) {

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ADMIN','THERAPIST')")
    fun createSession(
        @RequestBody request: PlanSessionRequest
    ): ResponseEntity<PlanSessionResponse> {
        val response = createPlanSession.execute(request)
        return ResponseEntity.status(HttpStatus.CREATED).body(response)
    }

    @GetMapping("/plan/{planId}")
    @PreAuthorize("hasAnyAuthority('ADMIN','THERAPIST','COMPANY')")
    fun getSessionsByPlan(
        @PathVariable planId: Int
    ): ResponseEntity<List<PlanSessionResponse>> {
        return ResponseEntity.ok(getPlanSessionsByPlan.execute(planId))
    }
}
