package com.lira.application.progressplan

import com.lira.domain.progressplan.TranscriptionResult
import com.lira.domain.progressplan.TranscriptionService
import org.springframework.stereotype.Service

@Service
class TranscribeAudioDraft(
    private val transcriptionService: TranscriptionService
) {
    fun execute(audioBytes: ByteArray, mimeType: String): TranscriptionResult =
        transcriptionService.transcribe(audioBytes, mimeType)
}
