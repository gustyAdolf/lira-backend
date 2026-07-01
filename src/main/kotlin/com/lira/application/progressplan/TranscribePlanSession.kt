package com.lira.application.progressplan

import com.lira.domain.progressplan.PlanSessionRepository
import com.lira.domain.progressplan.TranscriptionResult
import com.lira.domain.progressplan.TranscriptionService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class TranscribePlanSession(
    private val planSessionRepository: PlanSessionRepository,
    private val transcriptionService: TranscriptionService
) {
    private val log = LoggerFactory.getLogger(javaClass)

    fun execute(planSessionId: Int, audioBytes: ByteArray, mimeType: String): TranscriptionResult {
        val session = planSessionRepository.findById(planSessionId)
            ?: throw NoSuchElementException("PlanSession not found: $planSessionId")

        val result = transcriptionService.transcribe(audioBytes, mimeType)

        val updated = session.copy(transcript = result.transcript, aiSummary = result.aiSummary)
        planSessionRepository.save(updated)

        log.info("Transcription saved for planSession=$planSessionId, summaryPresent=${result.aiSummary != null}")
        return result
    }
}
