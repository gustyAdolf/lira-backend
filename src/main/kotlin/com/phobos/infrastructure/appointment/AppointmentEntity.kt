package com.phobos.infrastructure.appointment

import com.phobos.domain.appointment.Appointment
import com.phobos.infrastructure.mentaldisorder.MentalDisorderEntity
import com.phobos.infrastructure.mentaldisorder.toDomain
import com.phobos.infrastructure.user.TherapistEntity
import com.phobos.infrastructure.user.entity.PatientEntity
import com.phobos.infrastructure.user.entity.toDomain
import com.phobos.infrastructure.user.toDomain
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
    val status: AppointmentStatus
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
