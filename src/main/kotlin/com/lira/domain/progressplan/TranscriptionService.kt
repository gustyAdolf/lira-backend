package com.lira.domain.progressplan

interface TranscriptionService {
    fun transcribe(audioBytes: ByteArray, mimeType: String): TranscriptionResult
}
