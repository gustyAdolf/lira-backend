package com.lira.application.patienttask

import com.lira.domain.patienttask.PatientTaskEntryRepository
import com.lira.domain.patienttask.PatientTaskJournalEntry
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class GetPatientJournalForTherapist(
    private val patientTaskEntryRepository: PatientTaskEntryRepository,
) {
    fun execute(patientId: Int): List<PatientTaskJournalEntry> =
        patientTaskEntryRepository.findJournalForPatient(patientId)
}
