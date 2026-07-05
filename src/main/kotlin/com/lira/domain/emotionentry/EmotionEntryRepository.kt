package com.lira.domain.emotionentry

import org.springframework.data.domain.Sort
import java.time.LocalDateTime

interface EmotionEntryRepository {
    fun save(emotion: EmotionEntry): EmotionEntry
    fun findByUserId(userId: Int, startDate: LocalDateTime, endDate: LocalDateTime, sort: Sort): List<EmotionEntry>
}