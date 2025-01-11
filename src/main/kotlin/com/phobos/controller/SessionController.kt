package com.phobos.controller

import com.phobos.application.dto.session.SessionRequest
import com.phobos.application.dto.session.SessionResponse
import com.phobos.application.service.SessionService
import com.phobos.application.util.PageableUtil
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/session")
class SessionController(@Autowired val sessionService: SessionService) {

    @GetMapping
    fun getSessionBy(
        @RequestParam(value = "userId", required = true) userId: Int,
        @RequestParam(value = "mentalDisorderId", required = true) mentalDisorderId: Int,
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "10") size: Int,
        @RequestParam(value = "dateOrder", defaultValue = "sessionDate,desc") dateOrder: String
    ): ResponseEntity<Page<SessionResponse>> {
        val pageable = PageableUtil.getPageableFrom(page, size, dateOrder)
        val sessions: Page<SessionResponse> = sessionService.getSessions(userId, mentalDisorderId, pageable)
        return ResponseEntity.ok(sessions)
    }

    @PostMapping
    fun newSession(@RequestBody session: SessionRequest): ResponseEntity<Void> {
        sessionService.addSession(session)
        return ResponseEntity.ok().build()
    }
}