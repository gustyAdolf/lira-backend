package com.lira.application.session

import com.lira.domain.session.Session
import com.lira.domain.session.SessionRepository
import com.lira.infrastructure.session.SessionRequest
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class CreateSessionTest {

    private lateinit var sessionRepository: SessionRepository
    private lateinit var createSession: CreateSession

    @BeforeEach
    fun setUp() {
        sessionRepository = mockk()
        createSession = CreateSession(sessionRepository)
    }

    @Test
    fun `execute construye la sesion con los datos del request`() {
        // given
        val request = SessionRequest(
            activationLevel = 50,
            exposureLevel = 30,
            userId = 3,
            mentalDisorderId = 1,
            progress = 25,
            therapistNotes = "Sesión positiva",
            userNotes = "Me sentí bien"
        )
        val sessionSlot = slot<Session>()
        every { sessionRepository.createSession(capture(sessionSlot)) } answers { sessionSlot.captured }

        // when
        createSession.execute(request)

        // then
        val captured = sessionSlot.captured
        assertEquals(50, captured.activationLevel)
        assertEquals(30, captured.exposureLevel)
        assertEquals(25, captured.progress)
        assertEquals(3, captured.userId)
        assertEquals(1, captured.mentalDisorderId)
    }

    @Test
    fun `execute devuelve la sesion tal como la persiste el repositorio`() {
        // given
        val request = SessionRequest(
            activationLevel = 70,
            exposureLevel = 40,
            userId = 5,
            mentalDisorderId = 2,
            progress = 60,
            therapistNotes = "Buen progreso",
            userNotes = "Algo de ansiedad"
        )
        val persistedSession = Session(
            id = 99,
            activationLevel = request.activationLevel,
            exposureLevel = request.exposureLevel,
            userId = request.userId,
            mentalDisorderId = request.mentalDisorderId,
            progress = request.progress,
            therapistNotes = request.therapistNotes,
            userNotes = request.userNotes
        )
        every { sessionRepository.createSession(any()) } returns persistedSession

        // when
        val result = createSession.execute(request)

        // then
        assertEquals(99, result.id)
        assertEquals(persistedSession, result)
    }
}
