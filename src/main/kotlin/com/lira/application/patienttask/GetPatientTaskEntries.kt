package com.lira.application.patienttask

import com.lira.domain.patienttask.PatientTaskEntry
import com.lira.domain.patienttask.PatientTaskEntryRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class GetPatientTaskEntries(
    private val patientTaskEntryRepository: PatientTaskEntryRepository,
) {
    fun execute(patientId: Int): List<PatientTaskEntry> = patientTaskEntryRepository.findByPatientId(patientId)
}
