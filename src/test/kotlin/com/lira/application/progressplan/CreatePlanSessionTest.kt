package com.lira.application.progressplan

import com.lira.domain.progressplan.PlanSession
import com.lira.domain.progressplan.PlanSessionRepository
import com.lira.infrastructure.progressplan.dto.PlanSessionRequest
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

class CreatePlanSessionTest {

    private lateinit var planSessionRepository: PlanSessionRepository
    private lateinit var createPlanSession: CreatePlanSession

    @BeforeEach
    fun setUp() {
        planSessionRepository = mockk()
        createPlanSession = CreatePlanSession(planSessionRepository)
    }

    @Test
    fun `execute persiste la sesion y devuelve respuesta con id`() {
        // given
        val request = PlanSessionRequest(planId = 1, therapistId = 6, notes = "Buena sesión")
        val saved = PlanSession(id = 42, planId = 1, therapistId = 6, notes = "Buena sesión",
            sessionDate = LocalDateTime.now())
        every { planSessionRepository.save(any()) } returns saved

        // when
        val result = createPlanSession.execute(request)

        // then
        assertEquals(42, result.id)
        assertEquals(1, result.planId)
        assertEquals("Buena sesión", result.notes)
        verify(exactly = 1) { planSessionRepository.save(any()) }
    }

    @Test
    fun `execute persiste sesion sin notas`() {
        // given
        val request = PlanSessionRequest(planId = 2, therapistId = 6, notes = null)
        val saved = PlanSession(id = 5, planId = 2, therapistId = 6, notes = null,
            sessionDate = LocalDateTime.now())
        val slot = slot<PlanSession>()
        every { planSessionRepository.save(capture(slot)) } returns saved

        // when
        createPlanSession.execute(request)

        // then
        assertNull(slot.captured.notes)
        verify(exactly = 1) { planSessionRepository.save(any()) }
    }
}
