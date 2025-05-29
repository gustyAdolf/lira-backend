package com.phobos.infrastructure.treatment

import jakarta.persistence.*

@Entity
@Table(name = "treatment")
data class Treatment(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int,
    @Column(name = "user_id")
    val userId: Int,
    @Column(name = "progress_percentage")
    val progressPercentage: Int,
    val phobia: String
)
