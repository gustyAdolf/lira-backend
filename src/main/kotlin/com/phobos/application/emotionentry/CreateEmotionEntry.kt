package com.phobos.application.emotionentry

import com.phobos.domain.emotionentry.EmotionEntry
import com.phobos.domain.emotionentry.EmotionEntryRepository
import com.phobos.infrastructure.emotionentry.dto.EmotionEntryRequest
import com.phobos.infrastructure.emotionentry.dto.toDomain
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