package com.lira.infrastructure.report

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.lira.domain.appointment.toEntity
import com.lira.domain.report.Report
import com.lira.domain.report.ReportRepository
import com.lira.domain.report.ReportTypeEnum
import com.lira.domain.report.toEntity
import com.lira.infrastructure.mentaldisorder.MentalDisorderEntity
import com.lira.infrastructure.reportType.ReportTypeEntity
import com.lira.infrastructure.user.entity.PatientEntity
import com.lira.infrastructure.user.entity.TherapistEntity
import com.lira.infrastructure.user.entity.toDomain
import jakarta.persistence.EntityManager
import jakarta.transaction.Transactional
import org.springframework.stereotype.Repository

@Repository
class JpaReportRepositoryAdapter(
    private val jpaReportRepository: JpaReportRepository,
    private val entityManager: EntityManager,
    private val objectMapper: ObjectMapper
) : ReportRepository {

    override fun save(report: Report): Report {

        val therapistRef = entityManager.getReference(TherapistEntity::class.java, report.therapist.id)
        val patientRef = entityManager.getReference(PatientEntity::class.java, report.patient.id)
        val reportTypeRef =
            entityManager.getReference(ReportTypeEntity::class.java, report.type.id)

        val entity = report.toEntity(therapistRef,
            patientRef,
            reportTypeRef)

        val saveReport = jpaReportRepository.save(entity);

        return jpaReportRepository.findById(saveReport.id)
            .map (ReportEntity::toDomain).orElseThrow();
    }

    override fun findByTherapistIdOrderByCreatedAtDesc(therapistId: Long): List<Report> {
        return jpaReportRepository.findByTherapistIdOrderByCreatedAtDesc(therapistId)
            .map { it.toDomain() }
    }

    override fun findById(id: Long): Report? {
        // 1. Buscamos la entidad
        val entity = jpaReportRepository.findById(id.toInt()).orElse(null) ?: return null

        // 2. Convertimos el JSON de la base de datos a un Mapa mutable
        val dataMap: MutableMap<String, Any> = try {
            objectMapper.convertValue(entity.data, object : TypeReference<MutableMap<String, Any>>() {})
        } catch (e: Exception) {
            mutableMapOf()
        }

        // 3. MOCKEAMOS LOS DATOS DE LA COMPANY
        // Estos campos coinciden con las variables {{centro_...}} que definimos en el HTML
        dataMap["centro_nombre"] = "Centro de Salud Lira"
        dataMap["centro_nif"] = "B-12345678"
        dataMap["centro_registro_sanitario"] = "CS998877"
        dataMap["centro_direccion"] = "Calle de la Innovación 42, 28001 Madrid"
        dataMap["centro_telefono"] = "910 00 00 00"
        dataMap["centro_email"] = "contacto@lira-clinica.com"
        dataMap["centro_web"] = "www.lira-clinica.com"
        dataMap["ciudad"] = "Madrid"

        // Datos adicionales del profesional que no están en la clase Therapist (Mockeados)
        dataMap["pro_cargo_especialidad"] = "Psicólogo General Sanitario"

        // 4. Devolvemos el Report con el mapa enriquecido
        return Report(
            id = entity.id,
            patient = entity.patient.toDomain(),
            therapist = entity.therapist.toDomain(), // Aquí ya viene el licenseNumber
            type = ReportTypeEnum.fromId(entity.reportType.id) ?: ReportTypeEnum.DERIVATION_REPORT,
            data = dataMap,
            createdAt = entity.createdAt
        )
    }

}