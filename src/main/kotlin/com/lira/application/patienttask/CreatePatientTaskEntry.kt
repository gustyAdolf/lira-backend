package com.lira.application.patienttask

import com.lira.domain.emotionentry.EmotionEntryRepository
import com.lira.domain.exceptions.PatientTaskException
import com.lira.domain.patienttask.PatientTaskEntry
import com.lira.domain.patienttask.PatientTaskEntryRepository
import com.lira.infrastructure.emotionentry.dto.toDomain
import com.lira.infrastructure.patienttask.dto.PatientTaskEntryRequest
import com.lira.infrastructure.patienttask.dto.toEmotionEntryRequest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class CreatePatientTaskEntry(
    private val patientTaskEntryRepository: PatientTaskEntryRepository,
    private val emotionEntryRepository: EmotionEntryRepository,
) {
    fun execute(request: PatientTaskEntryRequest): PatientTaskEntry {
        if ((request.subobjectiveId == null) == (request.patientTaskId == null)) {
            throw PatientTaskException.InvalidEntryTarget()
        }

        val savedEmotion = emotionEntryRepository.save(request.toEmotionEntryRequest().toDomain())

        val entry = PatientTaskEntry(
            patientId = request.patientId,
            subobjectiveId = request.subobjectiveId,
            patientTaskId = request.patientTaskId,
            note = request.note,
            emotionEntryId = savedEmotion.id,
        )
        return patientTaskEntryRepository.save(entry)
    }
}
