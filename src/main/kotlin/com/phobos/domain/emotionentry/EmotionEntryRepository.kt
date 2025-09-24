package com.phobos.domain.emotionentry

interface EmotionEntryRepository {
    fun save(emotion: EmotionEntry)
    fun findByUserId(userId: Int): List<EmotionEntry>
}