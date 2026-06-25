package com.lira.application.progressplan

import com.lira.domain.progressplan.Objective
import com.lira.domain.progressplan.ProgressPlan
import com.lira.domain.progressplan.ProgressPlanRepository
import com.lira.domain.progressplan.Subobjective
import com.lira.domain.progressplan.SubobjectiveEntry
import com.lira.domain.progressplan.SubobjectiveType
import com.lira.domain.user.Patient
import com.lira.domain.user.Therapist
import com.lira.infrastructure.progressplan.dto.SubobjectiveEntryRequest
import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.slot
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class RegisterProgressTest {

    private lateinit var progressPlanRepository: ProgressPlanRepository
    private lateinit var registerProgress: RegisterProgress

    @BeforeEach
    fun setUp() {
        progressPlanRepository = mockk()
        registerProgress = RegisterProgress(progressPlanRepository)
    }

    @Test
    fun `execute propaga appointmentId al guardar la entry`() {
        // given
        val appointmentId = 7
        val request = SubobjectiveEntryRequest(
            subobjectiveId = 1,
            objectiveId = 2,
            therapistId = 6,
            appointmentId = appointmentId,
            valueIncrement = 5,
            isSuccess = true,
            note = "Sesión con cita vinculada"
        )
        val entrySlot = slot<SubobjectiveEntry>()
        setupQuantitativeMocks(entrySlot)

        // when
        registerProgress.execute(request)

        // then
        assertEquals(appointmentId, entrySlot.captured.appointmentId)
    }

    @Test
    fun `execute guarda entry sin appointmentId cuando no hay cita`() {
        // given
        val request = SubobjectiveEntryRequest(
            subobjectiveId = 1,
            objectiveId = 2,
            therapistId = 6,
            appointmentId = null,
            valueIncrement = 3,
            isSuccess = true,
            note = "Registro manual"
        )
        val entrySlot = slot<SubobjectiveEntry>()
        setupQuantitativeMocks(entrySlot)

        // when
        registerProgress.execute(request)

        // then
        assertEquals(null, entrySlot.captured.appointmentId)
    }

    private fun setupQuantitativeMocks(entrySlot: io.mockk.CapturingSlot<SubobjectiveEntry>) {
        val subobjective = Subobjective(
            id = 1,
            title = "Reducir cigarrillos",
            description = null,
            type = SubobjectiveType.QUANTITATIVE,
            targetValue = 20,
            targetSuccess = null,
            targetFail = null
        )
        val plan = ProgressPlan(
            id = 1,
            patient = Patient(id = 3),
            therapist = Therapist(id = 6),
            title = "Plan test",
            description = null,
            objectives = listOf(
                Objective(id = 2, title = "Objetivo", description = null, orderIndex = 0,
                    subobjectives = listOf(subobjective))
            )
        )
        justRun { progressPlanRepository.saveSubobjectiveEntry(capture(entrySlot)) }
        every { progressPlanRepository.getSubobjectiveById(1) } returns subobjective
        every { progressPlanRepository.sumValueBySubobjective(1) } returns 5.0
        justRun { progressPlanRepository.saveSubobjective(any(), any()) }
        every { progressPlanRepository.getProgressPlanBySubobjectiveId(1) } returns plan
        justRun { progressPlanRepository.save(any()) }
    }
}
