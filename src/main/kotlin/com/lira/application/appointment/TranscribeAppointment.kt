package com.lira.application.appointment

import com.lira.domain.appointment.InitialAssessment
import com.lira.domain.appointment.InitialAssessmentRepository
import com.lira.domain.progressplan.TranscriptionResult
import com.lira.domain.progressplan.TranscriptionService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class TranscribeAppointment(
    private val initialAssessmentRepository: InitialAssessmentRepository,
    private val transcriptionService: TranscriptionService,
) {
    private val log = LoggerFactory.getLogger(javaClass)

    fun execute(appointmentId: Int, audioBytes: ByteArray, mimeType: String): TranscriptionResult {
        val result = transcriptionService.transcribe(audioBytes, mimeType)

        val existing = initialAssessmentRepository.findByAppointmentId(appointmentId)
        val updated = existing?.copy(aiSummary = result.aiSummary)
            ?: InitialAssessment(appointmentId = appointmentId, aiSummary = result.aiSummary)
        initialAssessmentRepository.save(updated)

        log.info("Transcription saved for appointment=$appointmentId, summaryPresent=${result.aiSummary != null}")
        return result
    }
}
