package com.phobos.infrastructure.persistence

import com.phobos.infrastructure.persistence.user.User
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "appointment")
data class Appointment(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int = 0,
    @Column(name = "therapist_id")
    val therapistId: Long,
    @ManyToOne
    @JoinColumn(name = "user_id")
    val user: User,
    @ManyToOne
    @JoinColumn(name = "mental_disorder_id")
    val mentalDisorder: MentalDisorder,
    @Column(name = "appointment_date")
    val appointmentDate: LocalDateTime,
    @Column(name = "appointment_duration")
    val appointmentDuration: Int,
    @Column(name = "description")
    val description: String?,
)
