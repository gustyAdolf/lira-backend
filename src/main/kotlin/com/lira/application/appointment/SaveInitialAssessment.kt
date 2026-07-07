package com.lira.application.appointment

import com.lira.domain.appointment.InitialAssessment
import com.lira.domain.appointment.InitialAssessmentRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
@Transactional
class SaveInitialAssessment(private val repository: InitialAssessmentRepository) {

    fun execute(appointmentId: Int, patch: InitialAssessmentPatch): InitialAssessment {
        val existing = repository.findByAppointmentId(appointmentId)
        val base = existing ?: InitialAssessment(appointmentId = appointmentId)
        val updated = base.copy(
            chiefComplaint = patch.chiefComplaint ?: base.chiefComplaint,
            background = patch.background ?: base.background,
            sessionNotes = patch.sessionNotes ?: base.sessionNotes,
            nextSteps = patch.nextSteps ?: base.nextSteps,
            transcript = patch.transcript ?: base.transcript,
            aiSummary = patch.aiSummary ?: base.aiSummary,
            audioLocalPath = patch.audioLocalPath ?: base.audioLocalPath,
            updatedAt = LocalDateTime.now(),
        )
        return repository.save(updated)
    }
}

data class InitialAssessmentPatch(
    val chiefComplaint: String? = null,
    val background: String? = null,
    val sessionNotes: String? = null,
    val nextSteps: String? = null,
    val transcript: String? = null,
    val aiSummary: String? = null,
    val audioLocalPath: String? = null,
)
