package com.lira.infrastructure.user

import com.lira.domain.user.PatientDisorderRepository
import com.lira.infrastructure.mentaldisorder.MentalDisorderEntity
import com.lira.infrastructure.user.entity.PatientEntity
import com.lira.infrastructure.user.jpa.JpaUserDisorderRepository
import jakarta.persistence.EntityManager
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Repository

@Repository
class JpaPatientDisorderRepositoryAdapter(
    private val jpaUserDisorderRepository: JpaUserDisorderRepository,
    private val entityManager: EntityManager
) : PatientDisorderRepository {

    private val log = LoggerFactory.getLogger(javaClass)

    override fun assignIfAbsent(patientId: Int, mentalDisorderId: Int) {
        try {
            val id = UserDisorderId(patientId, mentalDisorderId)
            if (!jpaUserDisorderRepository.existsById(id)) {
                val patient = entityManager.getReference(PatientEntity::class.java, patientId)
                val disorder = entityManager.getReference(MentalDisorderEntity::class.java, mentalDisorderId)
                jpaUserDisorderRepository.save(UserDisorderEntity(id = id, patient = patient, mentalDisorder = disorder))
            }
        } catch (e: Exception) {
            log.warn("No se pudo asignar trastorno $mentalDisorderId al paciente $patientId: ${e.message}")
        }
    }
}
