package com.phobos.infrastructure.session

import com.phobos.application.session.CreateSession
import com.phobos.application.session.GetSessions
import com.phobos.infrastructure.util.PageableUtil
import org.springframework.data.domain.Page
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/session")
class SessionController(
    private val getSessions: GetSessions,
    private val createSession: CreateSession,
) {

    @GetMapping
    fun getSessionBy(
        @RequestParam(value = "userId", required = true) userId: Int,
        @RequestParam(value = "mentalDisorderId", required = true) mentalDisorderId: Int,
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "10") size: Int,
        @RequestParam(value = "dateOrder", defaultValue = "sessionDate,desc") dateOrder: String
    ): ResponseEntity<Page<SessionResponse>> {

        val pageable = PageableUtil.getPageableFrom(page, size, dateOrder)
        val sessions = getSessions.execute(userId, mentalDisorderId, pageable)
        return ResponseEntity.ok(sessions.map { it.toResponse() })
    }

    @PostMapping
    fun newSession(@RequestBody session: SessionRequest): ResponseEntity<Void> {
        createSession.execute(session)
        return ResponseEntity.ok().build()
    }
}