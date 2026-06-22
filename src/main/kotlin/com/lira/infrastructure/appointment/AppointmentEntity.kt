package com.lira.infrastructure.appointment

import com.lira.domain.appointment.Appointment
import com.lira.domain.appointment.AppointmentStatus
import com.lira.infrastructure.mentaldisorder.MentalDisorderEntity
import com.lira.infrastructure.mentaldisorder.toDomain
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

    @ManyToOne
    @JoinColumn(name = "mental_disorder_id")
    val mentalDisorder: MentalDisorderEntity,

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
    val status: AppointmentStatus  // domain enum — JPA serializes by name
)

fun AppointmentEntity.toDomain(): Appointment {
    return Appointment(
        id = this.id,
        therapist = this.therapist.toDomain(),
        patient = this.patient.toDomain(),
        mentalDisorder = this.mentalDisorder.toDomain(),
        appointmentDate = this.appointmentDate,
        appointmentDuration = this.appointmentDuration,
        description = this.description,
        cost = this.cost,
        status = this.status
    )
}

fun Appointment.toEntity(
    therapistEntity: TherapistEntity,
    patientEntity: PatientEntity,
    mentalDisorderEntity: MentalDisorderEntity
): AppointmentEntity = AppointmentEntity(
    therapist = therapistEntity,
    patient = patientEntity,
    mentalDisorder = mentalDisorderEntity,
    appointmentDate = appointmentDate,
    appointmentDuration = appointmentDuration,
    description = description,
    cost = cost,
    status = status
)
