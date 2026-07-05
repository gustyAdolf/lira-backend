package com.lira.domain.patienttask

interface PatientTaskRepository {
    fun save(task: PatientTask): PatientTask
    fun findByPatientId(patientId: Int): List<PatientTask>
    fun findById(id: Int): PatientTask?
    fun deleteById(id: Int)
}
