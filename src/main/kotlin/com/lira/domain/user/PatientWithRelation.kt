package com.lira.domain.user

data class PatientWithRelation(
    val patient: Patient,
    val isMyPatient: Boolean
)
