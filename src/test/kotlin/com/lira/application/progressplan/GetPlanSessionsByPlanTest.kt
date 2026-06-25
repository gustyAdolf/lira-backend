package com.lira.application.progressplan

import com.lira.domain.progressplan.PlanSession
import com.lira.domain.progressplan.PlanSessionRepository
import com.lira.domain.progressplan.ProgressPlanRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

class GetPlanSessionsByPlanTest {

    private lateinit var planSessionRepository: PlanSessionRepository
    private lateinit var progressPlanRepository: ProgressPlanRepository
    private lateinit var getPlanSessionsByPlan: GetPlanSessionsByPlan

    @BeforeEach
    fun setUp() {
        planSessionRepository = mockk()
        progressPlanRepository = mockk()
        getPlanSessionsByPlan = GetPlanSessionsByPlan(planSessionRepository, progressPlanRepository)
    }

    @Test
    fun `execute devuelve sesiones ordenadas con sus entries`() {
        // given
        val planId = 1
        val session1 = PlanSession(id = 2, planId = planId, therapistId = 6,
            sessionDate = LocalDateTime.of(2026, 6, 20, 10, 0))
        val session2 = PlanSession(id = 1, planId = planId, therapistId = 6,
            sessionDate = LocalDateTime.of(2026, 6, 10, 10, 0))
        every { planSessionRepository.findByPlanId(planId) } returns listOf(session1, session2)
        every { progressPlanRepository.findEntriesByPlanSessionId(any()) } returns emptyList()

        // when
        val result = getPlanSessionsByPlan.execute(planId)

        // then
        assertEquals(2, result.size)
        assertEquals(2, result[0].id)
        assertEquals(1, result[1].id)
        verify(exactly = 2) { progressPlanRepository.findEntriesByPlanSessionId(any()) }
    }

    @Test
    fun `execute devuelve lista vacia cuando el plan no tiene sesiones`() {
        // given
        val planId = 99
        every { planSessionRepository.findByPlanId(planId) } returns emptyList()

        // when
        val result = getPlanSessionsByPlan.execute(planId)

        // then
        assertEquals(0, result.size)
        verify(exactly = 0) { progressPlanRepository.findEntriesByPlanSessionId(any()) }
    }
}
