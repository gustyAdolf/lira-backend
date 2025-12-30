package com.lira.infrastructure.report

import com.lira.domain.appointment.toEntity
import com.lira.domain.report.Report
import com.lira.domain.report.ReportRepository
import com.lira.domain.report.toEntity
import com.lira.infrastructure.mentaldisorder.MentalDisorderEntity
import com.lira.infrastructure.reportType.ReportTypeEntity
import com.lira.infrastructure.user.entity.PatientEntity
import com.lira.infrastructure.user.entity.TherapistEntity
import jakarta.persistence.EntityManager
import org.springframework.stereotype.Repository

@Repository
class JpaReportRepositoryAdapter(
    private val jpaReportRepository: JpaReportRepository,
    private val entityManager: EntityManager
) : ReportRepository {

    override fun save(report: Report): Report {

        val therapistRef = entityManager.getReference(TherapistEntity::class.java, report.therapist.id)
        val patientRef = entityManager.getReference(PatientEntity::class.java, report.patient.id)
        val reportTypeRef =
            entityManager.getReference(ReportTypeEntity::class.java, report.type.id)

        val entity = report.toEntity(therapistRef,
            patientRef,
            reportTypeRef)

        return jpaReportRepository.save(entity).toDomain()
    }
}