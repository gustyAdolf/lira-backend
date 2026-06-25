package com.lira.application.progressplan

import com.lira.domain.progressplan.ProgressPlanRepository
import com.lira.domain.user.PatientDisorderRepository
import com.lira.infrastructure.progressplan.dto.ProgressPlanRequest
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class CreateProgressPlanTest {

    private lateinit var progressPlanRepository: ProgressPlanRepository
    private lateinit var patientDisorderRepository: PatientDisorderRepository
    private lateinit var createProgressPlan: CreateProgressPlan

    @BeforeEach
    fun setUp() {
        progressPlanRepository = mockk()
        patientDisorderRepository = mockk()
        createProgressPlan = CreateProgressPlan(progressPlanRepository, patientDisorderRepository)
        justRun { progressPlanRepository.save(any()) }
    }

    @Test
    fun `execute asigna el trastorno al paciente cuando se indica mentalDisorderId`() {
        // given
        val request = ProgressPlanRequest(
            patientId = 3,
            therapistId = 6,
            title = "Plan ansiedad social",
            description = null,
            mentalDisorderId = 1,
            objectives = emptyList()
        )
        justRun { patientDisorderRepository.assignIfAbsent(3, 1) }

        // when
        createProgressPlan.execute(request)

        // then
        verify(exactly = 1) { patientDisorderRepository.assignIfAbsent(3, 1) }
        verify(exactly = 1) { progressPlanRepository.save(any()) }
    }

    @Test
    fun `execute no llama assignIfAbsent cuando no se indica mentalDisorderId`() {
        // given
        val request = ProgressPlanRequest(
            patientId = 3,
            therapistId = 6,
            title = "Plan general",
            description = null,
            mentalDisorderId = null,
            objectives = emptyList()
        )

        // when
        createProgressPlan.execute(request)

        // then
        verify(exactly = 0) { patientDisorderRepository.assignIfAbsent(any(), any()) }
        verify(exactly = 1) { progressPlanRepository.save(any()) }
    }
}
