package com.lira.infrastructure.appointment

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface JpaInitialAssessmentRepository : JpaRepository<InitialAssessmentEntity, Int> {
    fun findByAppointmentId(appointmentId: Int): InitialAssessmentEntity?

    @Query(
        value = """
            SELECT ia.* FROM initial_assessment ia
            JOIN appointment a ON a.id = ia.appointment_id
            WHERE a.user_id = :patientId
              AND a.appointment_type = 'FIRST_VISIT'
            ORDER BY a.appointment_date ASC
            LIMIT 1
        """,
        nativeQuery = true
    )
    fun findFirstVisitAssessmentByPatientId(patientId: Int): InitialAssessmentEntity?

    @Query(
        value = """
            SELECT ia.id              AS "id",
                   ia.appointment_id  AS "appointmentId",
                   ia.chief_complaint AS "chiefComplaint",
                   ia.background      AS "background",
                   ia.session_notes   AS "sessionNotes",
                   ia.next_steps      AS "nextSteps",
                   ia.transcript      AS "transcript",
                   ia.ai_summary      AS "aiSummary",
                   ia.audio_local_path AS "audioLocalPath",
                   ia.created_at      AS "createdAt",
                   ia.updated_at      AS "updatedAt",
                   a.appointment_type AS "appointmentType",
                   a.appointment_date AS "appointmentDate"
            FROM initial_assessment ia
            JOIN appointment a ON a.id = ia.appointment_id
            WHERE a.user_id = :patientId
              AND a.appointment_type IN ('FIRST_VISIT', 'GENERAL')
            ORDER BY a.appointment_date DESC
        """,
        nativeQuery = true
    )
    fun findAllAssessmentsByPatientId(patientId: Int): List<PatientAssessmentView>
}
