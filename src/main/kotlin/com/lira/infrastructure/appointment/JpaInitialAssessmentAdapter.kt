package com.lira.infrastructure.appointment

import com.lira.domain.appointment.InitialAssessment
import com.lira.domain.appointment.InitialAssessmentRepository
import org.springframework.stereotype.Component

@Component
class JpaInitialAssessmentAdapter(
    private val jpaInitialAssessmentRepository: JpaInitialAssessmentRepository
) : InitialAssessmentRepository {

    override fun save(assessment: InitialAssessment): InitialAssessment =
        jpaInitialAssessmentRepository.save(assessment.toEntity()).toDomain()

    override fun findByAppointmentId(appointmentId: Int): InitialAssessment? =
        jpaInitialAssessmentRepository.findByAppointmentId(appointmentId)?.toDomain()

    override fun findFirstVisitByPatientId(patientId: Int): InitialAssessment? =
        jpaInitialAssessmentRepository.findFirstVisitAssessmentByPatientId(patientId)?.toDomain()

    override fun findAllByPatientId(patientId: Int): List<InitialAssessment> =
        jpaInitialAssessmentRepository.findAllAssessmentsByPatientId(patientId).map { v ->
            InitialAssessment(
                id = v.getId(),
                appointmentId = v.getAppointmentId(),
                chiefComplaint = v.getChiefComplaint(),
                background = v.getBackground(),
                sessionNotes = v.getSessionNotes(),
                nextSteps = v.getNextSteps(),
                transcript = v.getTranscript(),
                aiSummary = v.getAiSummary(),
                audioLocalPath = v.getAudioLocalPath(),
                createdAt = v.getCreatedAt(),
                updatedAt = v.getUpdatedAt(),
                appointmentType = v.getAppointmentType(),
                appointmentDate = v.getAppointmentDate(),
            )
        }
}
