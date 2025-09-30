package com.phobos.application.emotionentry

import com.phobos.domain.emotionentry.EmotionEntry
import com.phobos.domain.emotionentry.EmotionEntryRepository
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
@Transactional(readOnly = true)
class GetUserEmotionEntries(
    private val emotionEntryRepository: EmotionEntryRepository
) {
    fun execute(
        userId: Int,
        startDate: LocalDateTime,
        endDate: LocalDateTime,
        sort: Sort
    ): List<EmotionEntry> {
        return emotionEntryRepository.findByUserId(userId, startDate, endDate, sort)
    }
}