package com.lira.infrastructure.report

import com.lira.domain.report.Report
import com.lira.domain.report.ReportTypeEnum
import com.lira.domain.user.Patient
import com.lira.domain.user.Therapist
import com.lira.infrastructure.reportType.ReportTypeEntity
import com.lira.infrastructure.user.entity.PatientEntity
import com.lira.infrastructure.user.entity.TherapistEntity
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "reports")
data class ReportEntity(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int = 0,

    // 1. RELACIÓN JPA COMPLETA (@ManyToOne)
    @ManyToOne
    @JoinColumn(name = "therapist_id")
    val therapist: TherapistEntity, // Ahora es el objeto entidad

    // 2. RELACIÓN JPA COMPLETA (@ManyToOne)
    @ManyToOne
    @JoinColumn(name = "user_id") // Cambiado a user_id para ser consistente con Appointment
    val patient: PatientEntity, // Ahora es el objeto entidad

    // 3. RELACIÓN JPA COMPLETA (@ManyToOne)
    @ManyToOne
    @JoinColumn(name = "report_type_id")
    val reportType: ReportTypeEntity, // Ahora es el objeto entidad

    @Convert(converter = JsonToMapConverter::class)
    @Column(columnDefinition = "JSONB", name = "data_report")
    val data: Map<String, Any>,

    @Column(name = "created_at")
    val createdAt: LocalDateTime,
)

fun ReportEntity.toDomain(): Report {
    // En toDomain(), usamos el ID de la entidad relacionada
    val reportType = ReportTypeEnum.fromId(this.reportType.id)
        ?: throw IllegalStateException("Tipo de informe ID ${this.reportType.id} no reconocido.")

    return Report(
        id = this.id,
        therapist = Therapist(id = this.therapist.id),
        patient = Patient(id = this.patient.id),
        type = reportType,
        data = this.data,
        createdAt = this.createdAt
    )
}
