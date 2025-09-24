package com.phobos.infrastructure.emotionentry

import com.phobos.application.emotionentry.CreateEmotionEntry
import com.phobos.application.emotionentry.GetUserEmotionEntries
import com.phobos.infrastructure.emotionentry.dto.EmotionEntryRequest
import com.phobos.infrastructure.emotionentry.dto.EmotionEntryResponse
import com.phobos.infrastructure.emotionentry.dto.toResponse
import com.phobos.infrastructure.rest.ApiResponse
import com.phobos.infrastructure.rest.ApiResponseStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/emotions")
class EmotionEntryController(
    private val getUserEmotionEntries: GetUserEmotionEntries,
    private val createEmotionEntry: CreateEmotionEntry
) {

    @GetMapping("/user/{userId}")
    fun getEmotionsByUserId(@PathVariable userId: Int): List<EmotionEntryResponse> {
        val emotions = getUserEmotionEntries.execute(userId)
        return emotions.map { it.toResponse() }
    }

    @PostMapping
    fun createEmotionRegistry(
        @RequestBody emotionEntryRequest: EmotionEntryRequest
    ): ApiResponse<Unit> {
        createEmotionEntry.execute(emotionEntryRequest)
        return ApiResponse(ApiResponseStatus.SUCCESS, "Emoción registrada")
    }

}