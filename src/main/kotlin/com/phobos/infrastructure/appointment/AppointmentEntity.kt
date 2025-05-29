package com.phobos.infrastructure.appointment

import com.phobos.domain.appointment.Appointment
import com.phobos.infrastructure.mentaldisorder.MentalDisorderEntity
import com.phobos.infrastructure.user.UserEntity
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "appointment")
data class AppointmentEntity(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int = 0,
    @Column(name = "therapist_id")
    val therapistId: Long,
    @ManyToOne
    @JoinColumn(name = "user_id")
    val user: UserEntity,
    @ManyToOne
    @JoinColumn(name = "mental_disorder_id")
    val mentalDisorder: MentalDisorderEntity,
    @Column(name = "appointment_date")
    val appointmentDate: LocalDateTime,
    @Column(name = "appointment_duration")
    val appointmentDuration: Int,
    @Column(name = "description")
    val description: String?,
)

fun AppointmentEntity.toDomain(): Appointment {
    return Appointment(
        id = this.id,
        therapistId = this.therapistId.toInt(),
        appointmentDate = this.appointmentDate,
        appointmentDuration = this.appointmentDuration,
        userId = this.user.id,
        name = this.user.name,
        imageUrl = this.user.profileImagePath,
        telephone = this.user.telephone,
        email = this.user.email,
        mentalDisorderId = this.mentalDisorder.id,
        mentalDisorder = this.mentalDisorder.name,
        description = this.description,
    )
}
