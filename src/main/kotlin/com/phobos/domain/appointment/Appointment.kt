package com.phobos.domain.appointment

import com.phobos.domain.mentaldisorder.MentalDisorder
import com.phobos.domain.user.Patient
import com.phobos.domain.user.Therapist
import com.phobos.infrastructure.appointment.AppointmentEntity
import com.phobos.infrastructure.appointment.AppointmentStatus
import com.phobos.infrastructure.mentaldisorder.MentalDisorderEntity
import com.phobos.infrastructure.user.entity.TherapistEntity
import com.phobos.infrastructure.user.entity.PatientEntity
import java.math.BigDecimal
import java.time.LocalDateTime

data class Appointment(
    val id: Int = 0,
    val therapist: Therapist,
    val patient: Patient,
    val appointmentDate: LocalDateTime,
    val appointmentDuration: Int,
    val mentalDisorder: MentalDisorder,
    val description: String?,
    val status: AppointmentStatus,
    val cost: BigDecimal,
)

fun Appointment.toEntity(
    therapistEntity: TherapistEntity,
    patientEntity: PatientEntity,
    mentalDisorderEntity: MentalDisorderEntity
): AppointmentEntity =
    AppointmentEntity(
        therapist = therapistEntity,
        patient = patientEntity,
        mentalDisorder = mentalDisorderEntity,
        appointmentDate = this.appointmentDate,
        appointmentDuration = this.appointmentDuration,
        description = this.description,
        cost = this.cost,
        status = this.status
    )