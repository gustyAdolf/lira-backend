package com.phobos.application.emotionentry

import com.phobos.domain.emotionentry.EmotionEntry
import com.phobos.domain.emotionentry.EmotionEntryRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class GetUserEmotionEntries(
    private val emotionEntryRepository: EmotionEntryRepository
) {
    fun execute(userId: Int): List<EmotionEntry> {
        return emotionEntryRepository.findByUserId(userId)
    }
}