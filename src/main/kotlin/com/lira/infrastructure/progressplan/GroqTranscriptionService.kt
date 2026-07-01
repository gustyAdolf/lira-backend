package com.lira.infrastructure.progressplan

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.lira.domain.progressplan.TranscriptionResult
import com.lira.domain.progressplan.TranscriptionService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.ByteArrayResource
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.util.LinkedMultiValueMap
import org.springframework.web.client.RestTemplate

@Service
class GroqTranscriptionService(
    @Value("\${groq.api-key}") private val apiKey: String,
    @Value("\${groq.whisper-model}") private val whisperModel: String,
    @Value("\${groq.llm-model}") private val llmModel: String,
) : TranscriptionService {

    private val log = LoggerFactory.getLogger(javaClass)
    private val restTemplate = RestTemplate()

    override fun transcribe(audioBytes: ByteArray, mimeType: String): TranscriptionResult {
        val transcript = callWhisper(audioBytes, mimeType)
        val summary = try {
            callLlama(transcript)
        } catch (e: Exception) {
            log.warn("LLM summarization failed, returning transcript only: ${e.message}")
            null
        }
        return TranscriptionResult(transcript = transcript, aiSummary = summary)
    }

    private fun callWhisper(audioBytes: ByteArray, mimeType: String): String {
        val headers = HttpHeaders().apply {
            contentType = MediaType.MULTIPART_FORM_DATA
            setBearerAuth(apiKey)
        }

        val body = LinkedMultiValueMap<String, Any>()
        val audioResource = object : ByteArrayResource(audioBytes) {
            override fun getFilename() = "session.${mimeType.substringAfterLast('/')}"
        }
        body.add("file", audioResource)
        body.add("model", whisperModel)
        body.add("response_format", "text")
        body.add("language", "es")

        val response = restTemplate.postForEntity(
            "https://api.groq.com/openai/v1/audio/transcriptions",
            HttpEntity(body, headers),
            String::class.java
        )

        return response.body ?: throw IllegalStateException("Empty transcription response from Groq")
    }

    private fun callLlama(transcript: String): String {
        val headers = HttpHeaders().apply {
            contentType = MediaType.APPLICATION_JSON
            setBearerAuth(apiKey)
        }

        val prompt = """
            Eres un asistente clínico especializado en psicología. Analiza la siguiente transcripción de una sesión de terapia y genera un resumen estructurado en español con estas secciones:

            1. **Temas principales tratados**: Los asuntos centrales abordados durante la sesión.
            2. **Estado emocional del paciente**: Observaciones sobre el estado emocional y actitud del paciente.
            3. **Avances y logros**: Progresos observados respecto a objetivos terapéuticos.
            4. **Puntos a seguir**: Temas o tareas pendientes para próximas sesiones.

            Transcripción:
            $transcript
        """.trimIndent()

        val requestBody = mapOf(
            "model" to llmModel,
            "messages" to listOf(
                mapOf("role" to "system", "content" to "Eres un asistente clínico que resume sesiones de terapia psicológica de forma clara y profesional."),
                mapOf("role" to "user", "content" to prompt)
            ),
            "temperature" to 0.3,
            "max_tokens" to 1024
        )

        val response = restTemplate.postForEntity(
            "https://api.groq.com/openai/v1/chat/completions",
            HttpEntity(requestBody, headers),
            ChatCompletionResponse::class.java
        )

        return response.body?.choices?.firstOrNull()?.message?.content
            ?: throw IllegalStateException("Empty chat completion response from Groq")
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    data class ChatCompletionResponse(val choices: List<Choice>)

    @JsonIgnoreProperties(ignoreUnknown = true)
    data class Choice(val message: Message)

    @JsonIgnoreProperties(ignoreUnknown = true)
    data class Message(val content: String)
}
