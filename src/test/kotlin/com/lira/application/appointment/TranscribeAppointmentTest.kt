package com.lira.application.appointment

import com.lira.domain.appointment.InitialAssessment
import com.lira.domain.appointment.InitialAssessmentRepository
import com.lira.domain.progressplan.TranscriptionResult
import com.lira.domain.progressplan.TranscriptionService
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class TranscribeAppointmentTest {

    private lateinit var assessmentRepository: InitialAssessmentRepository
    private lateinit var transcriptionService: TranscriptionService
    private lateinit var transcribeAppointment: TranscribeAppointment

    private val audioBytes = byteArrayOf(1, 2, 3)
    private val mimeType = "audio/mp4"
    private val transcriptionResult = TranscriptionResult(
        transcript = "Texto de la sesión",
        aiSummary = "Resumen estructurado de la sesión"
    )

    @BeforeEach
    fun setUp() {
        assessmentRepository = mockk()
        transcriptionService = mockk()
        transcribeAppointment = TranscribeAppointment(assessmentRepository, transcriptionService)
        every { transcriptionService.transcribe(any(), any()) } returns transcriptionResult
    }

    @Test
    fun `transcripcion exitosa actualiza aiSummary en assessment existente`() {
        // given
        val existing = InitialAssessment(id = 1, appointmentId = 10, sessionNotes = "notas previas")
        every { assessmentRepository.findByAppointmentId(10) } returns existing
        val savedSlot = slot<InitialAssessment>()
        every { assessmentRepository.save(capture(savedSlot)) } answers { savedSlot.captured }

        // when
        val result = transcribeAppointment.execute(10, audioBytes, mimeType)

        // then
        assertEquals(transcriptionResult.aiSummary, result.aiSummary)
        assertEquals(transcriptionResult.transcript, result.transcript)
        verify(exactly = 1) { assessmentRepository.save(any()) }
        assertEquals("Resumen estructurado de la sesión", savedSlot.captured.aiSummary)
        assertEquals("notas previas", savedSlot.captured.sessionNotes) // no se pierden los datos existentes
    }

    @Test
    fun `transcripcion exitosa crea assessment nuevo si no existia`() {
        // given
        every { assessmentRepository.findByAppointmentId(99) } returns null
        val savedSlot = slot<InitialAssessment>()
        every { assessmentRepository.save(capture(savedSlot)) } answers { savedSlot.captured }

        // when
        val result = transcribeAppointment.execute(99, audioBytes, mimeType)

        // then
        assertNotNull(result.aiSummary)
        verify(exactly = 1) { assessmentRepository.save(any()) }
        assertEquals(99, savedSlot.captured.appointmentId)
        assertEquals("Resumen estructurado de la sesión", savedSlot.captured.aiSummary)
    }

    @Test
    fun `fallo del servicio de transcripcion no llama a save`() {
        // given
        every { assessmentRepository.findByAppointmentId(10) } returns InitialAssessment(appointmentId = 10)
        every { transcriptionService.transcribe(any(), any()) } throws RuntimeException("Groq unavailable")

        // when / then
        org.junit.jupiter.api.assertThrows<RuntimeException> {
            transcribeAppointment.execute(10, audioBytes, mimeType)
        }
        verify(exactly = 0) { assessmentRepository.save(any()) }
    }

    @Test
    fun `resultado devuelve transcript y aiSummary del servicio`() {
        // given
        every { assessmentRepository.findByAppointmentId(5) } returns InitialAssessment(appointmentId = 5)
        every { assessmentRepository.save(any()) } answers { firstArg() }

        // when
        val result = transcribeAppointment.execute(5, audioBytes, mimeType)

        // then
        assertEquals("Texto de la sesión", result.transcript)
        assertEquals("Resumen estructurado de la sesión", result.aiSummary)
    }
}
