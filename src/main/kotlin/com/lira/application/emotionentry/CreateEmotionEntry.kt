package com.lira.application.emotionentry

import com.lira.domain.emotionentry.EmotionEntryRepository
import com.lira.infrastructure.emotionentry.dto.EmotionEntryRequest
import com.lira.infrastructure.emotionentry.dto.toDomain
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class CreateEmotionEntry(
    private val emotionEntryRepository: EmotionEntryRepository
) {
    fun execute(emotionEntry: EmotionEntryRequest) {
        emotionEntryRepository.save(emotionEntry.toDomain())
    }
}