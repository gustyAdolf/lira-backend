package com.lira.application.appointment

import com.lira.domain.appointment.InitialAssessment
import com.lira.domain.appointment.InitialAssessmentRepository
import org.springframework.stereotype.Service

@Service
class GetInitialAssessment(private val repository: InitialAssessmentRepository) {

    fun execute(appointmentId: Int): InitialAssessment? =
        repository.findByAppointmentId(appointmentId)
}
