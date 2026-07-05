package com.lira.infrastructure.patienttask

import com.lira.domain.patienttask.PatientTaskEntry
import com.lira.domain.patienttask.PatientTaskEntryRepository
import com.lira.domain.patienttask.PatientTaskJournalEntry
import com.lira.infrastructure.emotionentry.JpaEmotionEntryRepository
import com.lira.infrastructure.progressplan.jpa.JpaSubobjectiveRepository
import org.springframework.stereotype.Repository

@Repository
class JpaPatientTaskEntryAdapter(
    private val jpaPatientTaskEntryRepository: JpaPatientTaskEntryRepository,
    private val jpaSubobjectiveRepository: JpaSubobjectiveRepository,
    private val jpaPatientTaskRepository: JpaPatientTaskRepository,
    private val jpaEmotionEntryRepository: JpaEmotionEntryRepository,
) : PatientTaskEntryRepository {

    override fun save(entry: PatientTaskEntry): PatientTaskEntry =
        jpaPatientTaskEntryRepository.save(entry.toEntity()).toDomain()

    override fun findByPatientId(patientId: Int): List<PatientTaskEntry> =
        jpaPatientTaskEntryRepository.findByPatientId(patientId).map { it.toDomain() }

    override fun findJournalForPatient(patientId: Int): List<PatientTaskJournalEntry> {
        val entries = jpaPatientTaskEntryRepository.findByPatientId(patientId)
        if (entries.isEmpty()) return emptyList()

        val subobjectiveTitles = jpaSubobjectiveRepository
            .findAllById(entries.mapNotNull { it.subobjectiveId })
            .associate { it.id to it.title }
        val taskTitles = jpaPatientTaskRepository
            .findAllById(entries.mapNotNull { it.patientTaskId })
            .associate { it.id to it.title }
        val emotions = jpaEmotionEntryRepository
            .findAllById(entries.mapNotNull { it.emotionEntryId })
            .associateBy { it.id }

        return entries
            .sortedByDescending { it.createdAt }
            .map { e ->
                val title = e.subobjectiveId?.let { subobjectiveTitles[it] }
                    ?: e.patientTaskId?.let { taskTitles[it] }
                    ?: "Tarea"
                val emotion = e.emotionEntryId?.let { emotions[it] }
                PatientTaskJournalEntry(
                    id = e.id,
                    createdAt = e.createdAt,
                    subobjectiveId = e.subobjectiveId,
                    patientTaskId = e.patientTaskId,
                    title = title,
                    note = e.note,
                    firstEmotion = emotion?.firstEmotion ?: "",
                    secondEmotion = emotion?.secondEmotion,
                    thirdEmotion = emotion?.thirdEmotion,
                    intensity = emotion?.intensity ?: 0,
                    emotionNotes = emotion?.notes,
                )
            }
    }
}
