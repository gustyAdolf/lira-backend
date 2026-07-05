package com.lira.domain.patienttask

interface PatientTaskEntryRepository {
    fun save(entry: PatientTaskEntry): PatientTaskEntry
    fun findByPatientId(patientId: Int): List<PatientTaskEntry>
    fun findJournalForPatient(patientId: Int): List<PatientTaskJournalEntry>
}
