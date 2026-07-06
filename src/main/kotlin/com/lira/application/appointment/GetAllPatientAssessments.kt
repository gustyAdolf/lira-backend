package com.lira.application.appointment

import com.lira.domain.appointment.InitialAssessment
import com.lira.domain.appointment.InitialAssessmentRepository
import org.springframework.stereotype.Service

@Service
class GetAllPatientAssessments(private val repository: InitialAssessmentRepository) {

    fun execute(patientId: Int): List<InitialAssessment> =
        repository.findAllByPatientId(patientId)
}
