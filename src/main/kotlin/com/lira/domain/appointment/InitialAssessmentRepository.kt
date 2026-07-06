package com.lira.domain.appointment

interface InitialAssessmentRepository {
    fun save(assessment: InitialAssessment): InitialAssessment
    fun findByAppointmentId(appointmentId: Int): InitialAssessment?
    fun findFirstVisitByPatientId(patientId: Int): InitialAssessment?
    fun findAllByPatientId(patientId: Int): List<InitialAssessment>
}
