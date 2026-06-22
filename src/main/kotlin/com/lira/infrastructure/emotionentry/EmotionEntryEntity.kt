package com.lira.infrastructure.emotionentry

import com.lira.domain.emotionentry.EmotionEntry
import com.lira.infrastructure.user.entity.PatientEntity
import com.lira.infrastructure.user.entity.toDomain
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "emotion_entry")
data class EmotionEntryEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int = 0,

    @ManyToOne
    @JoinColumn(name = "user_id")
    val patient: PatientEntity,

    @Column(name = "first_emotion")
    val firstEmotion: String,

    @Column(name = "second_emotion")
    val secondEmotion: String?,

    @Column(name = "third_emotion")
    val thirdEmotion: String?,

    @Column(name = "intensity")
    val intensity: Int,

    @Column(name = "notes")
    val notes: String?,

    @Column(name = "created_at")
    val createdAt: LocalDateTime,
)

fun EmotionEntryEntity.toDomain() =
    EmotionEntry(
        id = this.id,
        patient = this.patient.toDomain(),
        firstEmotion = this.firstEmotion,
        secondEmotion = this.secondEmotion,
        thirdEmotion = this.thirdEmotion,
        intensity = this.intensity,
        notes = this.notes,
        createdAt = this.createdAt,
    )

fun EmotionEntry.toEntity(patientEntity: PatientEntity) = EmotionEntryEntity(
    patient = patientEntity,
    firstEmotion = firstEmotion,
    secondEmotion = secondEmotion,
    thirdEmotion = thirdEmotion,
    intensity = intensity,
    notes = notes,
    createdAt = createdAt ?: LocalDateTime.now()
)