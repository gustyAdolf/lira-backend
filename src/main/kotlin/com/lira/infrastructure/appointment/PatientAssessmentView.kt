package com.lira.infrastructure.appointment

import java.time.LocalDateTime

interface PatientAssessmentView {
    fun getId(): Int
    fun getAppointmentId(): Int
    fun getChiefComplaint(): String?
    fun getBackground(): String?
    fun getSessionNotes(): String?
    fun getNextSteps(): String?
    fun getTranscript(): String?
    fun getAiSummary(): String?
    fun getAudioLocalPath(): String?
    fun getCreatedAt(): LocalDateTime
    fun getUpdatedAt(): LocalDateTime
    fun getAppointmentType(): String?
    fun getAppointmentDate(): LocalDateTime?
}
