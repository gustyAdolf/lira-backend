package com.lira.infrastructure.appointment

import com.lira.domain.appointment.Appointment
import com.lira.domain.appointment.AppointmentStatus
import com.lira.domain.appointment.AppointmentType
import com.lira.infrastructure.user.entity.TherapistEntity
import com.lira.infrastructure.user.entity.PatientEntity
import com.lira.infrastructure.user.entity.toDomain
import jakarta.persistence.*
import java.math.BigDecimal
import java.time.LocalDateTime

@Entity
@Table(name = "appointment")
data class AppointmentEntity(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int = 0,

    @ManyToOne
    @JoinColumn(name = "therapist_id")
    val therapist: TherapistEntity,

    @ManyToOne
    @JoinColumn(name = "user_id")
    val patient: PatientEntity,

    @Column(name = "progress_plan_id")
    val progressPlanId: Int? = null,

    @Enumerated(EnumType.STRING)
    @Column(name = "appointment_type")
    val appointmentType: AppointmentType = AppointmentType.GENERAL,

    @Column(name = "therapist_notes")
    val therapistNotes: String? = null,

    @Column(name = "appointment_date")
    val appointmentDate: LocalDateTime,

    @Column(name = "appointment_duration")
    val appointmentDuration: Int,

    @Column(name = "description")
    val description: String?,

    @Column(name = "cost")
    val cost: BigDecimal = BigDecimal.ZERO,

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    val status: AppointmentStatus,

    @Column(name = "company_id")
    val companyId: Int? = null,

    @Column(name = "patient_confirmed_at")
    val patientConfirmedAt: LocalDateTime? = null,
)

fun AppointmentEntity.toDomain(): Appointment = Appointment(
    id = this.id,
    therapist = this.therapist.toDomain(),
    patient = this.patient.toDomain(),
    progressPlanId = this.progressPlanId,
    appointmentType = this.appointmentType,
    therapistNotes = this.therapistNotes,
    appointmentDate = this.appointmentDate,
    appointmentDuration = this.appointmentDuration,
    description = this.description,
    cost = this.cost,
    status = this.status,
    companyId = this.companyId,
    patientConfirmedAt = this.patientConfirmedAt,
)

fun Appointment.toEntity(
    therapistEntity: TherapistEntity,
    patientEntity: PatientEntity
): AppointmentEntity = AppointmentEntity(
    therapist = therapistEntity,
    patient = patientEntity,
    progressPlanId = progressPlanId,
    appointmentType = appointmentType,
    therapistNotes = therapistNotes,
    appointmentDate = appointmentDate,
    appointmentDuration = appointmentDuration,
    description = description,
    cost = cost,
    status = status,
    companyId = companyId,
    patientConfirmedAt = patientConfirmedAt,
)
