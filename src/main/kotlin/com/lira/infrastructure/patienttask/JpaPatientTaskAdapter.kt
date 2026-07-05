package com.lira.infrastructure.patienttask

import com.lira.domain.patienttask.PatientTask
import com.lira.domain.patienttask.PatientTaskRepository
import org.springframework.stereotype.Repository

@Repository
class JpaPatientTaskAdapter(
    private val jpaPatientTaskRepository: JpaPatientTaskRepository,
) : PatientTaskRepository {

    override fun save(task: PatientTask): PatientTask =
        jpaPatientTaskRepository.save(task.toEntity()).toDomain()

    override fun findByPatientId(patientId: Int): List<PatientTask> =
        jpaPatientTaskRepository.findByPatientId(patientId).map { it.toDomain() }

    override fun findById(id: Int): PatientTask? =
        jpaPatientTaskRepository.findById(id).map { it.toDomain() }.orElse(null)

    override fun deleteById(id: Int) =
        jpaPatientTaskRepository.deleteById(id)
}
