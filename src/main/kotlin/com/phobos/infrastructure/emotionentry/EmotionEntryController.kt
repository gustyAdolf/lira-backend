package com.phobos.infrastructure.emotionentry

import com.phobos.application.emotionentry.CreateEmotionEntry
import com.phobos.application.emotionentry.GetUserEmotionEntries
import com.phobos.infrastructure.emotionentry.dto.EmotionEntryRequest
import com.phobos.infrastructure.emotionentry.dto.EmotionEntryResponse
import com.phobos.infrastructure.emotionentry.dto.toResponse
import com.phobos.infrastructure.rest.ApiResponse
import com.phobos.infrastructure.rest.ApiResponseStatus
import com.phobos.infrastructure.util.PageableUtil
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.web.bind.annotation.*
import java.time.LocalDateTime

@RestController
@RequestMapping("/emotions")
class EmotionEntryController(
    private val getUserEmotionEntries: GetUserEmotionEntries,
    private val createEmotionEntry: CreateEmotionEntry
) {

    @GetMapping("/user/{userId}")
    fun getEmotionsByUserId(
        @PathVariable userId: Int,
        @RequestParam(value = "startDate") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") startDate: LocalDateTime = defaultStartDate(),
        @RequestParam(value = "endDate") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") endDate: LocalDateTime = defaultEndDate(),
        @RequestParam(value = "sortBy", defaultValue = "createdAt") sortBy: String,
        @RequestParam(value = "direction", defaultValue = "ASC") direction: String,
    ): List<EmotionEntryResponse> {

        val emotions = getUserEmotionEntries.execute(
            userId,
            startDate,
            endDate,
            PageableUtil.getSort(direction, sortBy),
        )
        return emotions.map { it.toResponse() }
    }

    @PostMapping
    fun createEmotionRegistry(
        @RequestBody emotionEntryRequest: EmotionEntryRequest
    ): ApiResponse<Unit> {
        createEmotionEntry.execute(emotionEntryRequest)
        return ApiResponse(ApiResponseStatus.SUCCESS, "Emoción registrada")
    }

    fun defaultStartDate(): LocalDateTime {
        return LocalDateTime.now().minusDays(7)
    }

    private fun defaultEndDate(): LocalDateTime {
        return LocalDateTime.now().plusDays(7)
    }

}