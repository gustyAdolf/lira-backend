package com.lira.domain.user

interface PatientDisorderRepository {
    fun assignIfAbsent(patientId: Int, mentalDisorderId: Int)
}
