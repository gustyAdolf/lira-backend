package com.lira.infrastructure.progressplan

import com.lira.application.progressplan.CreatePlanSession
import com.lira.application.progressplan.GetPlanSessionsByPlan
import com.lira.application.progressplan.TranscribePlanSession
import com.lira.infrastructure.progressplan.dto.PlanSessionRequest
import com.lira.infrastructure.progressplan.dto.PlanSessionResponse
import com.lira.infrastructure.progressplan.dto.TranscriptionResponse
import com.lira.infrastructure.progressplan.dto.toResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.server.ResponseStatusException

private const val MAX_AUDIO_BYTES = 25 * 1024 * 1024L // 25 MB

@RestController
@RequestMapping("/plan-sessions")
class PlanSessionController(
    private val createPlanSession: CreatePlanSession,
    private val getPlanSessionsByPlan: GetPlanSessionsByPlan,
    private val transcribePlanSession: TranscribePlanSession
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

    @PostMapping("/{id}/transcribe")
    @PreAuthorize("hasAnyAuthority('ADMIN','THERAPIST')")
    fun transcribeSession(
        @PathVariable id: Int,
        @RequestParam("audio") audio: MultipartFile
    ): ResponseEntity<TranscriptionResponse> {
        if (audio.size > MAX_AUDIO_BYTES) {
            throw ResponseStatusException(
                HttpStatus.UNPROCESSABLE_ENTITY,
                "Audio demasiado largo; la sesión no debe superar ~55 minutos con la configuración actual"
            )
        }
        val result = try {
            transcribePlanSession.execute(id, audio.bytes, audio.contentType ?: "audio/aac")
        } catch (e: NoSuchElementException) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, e.message)
        } catch (e: Exception) {
            throw ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Error al procesar el audio: ${e.message}")
        }
        return ResponseEntity.ok(result.toResponse())
    }
}
