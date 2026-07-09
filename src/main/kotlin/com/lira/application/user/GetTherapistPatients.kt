package com.lira.application.user

import com.lira.domain.appointment.AppointmentRepository
import com.lira.domain.user.PatientWithRelation
import com.lira.domain.user.UserRepository
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class GetTherapistPatients(
    private val userRepository: UserRepository,
    private val appointmentRepository: AppointmentRepository
) {
    fun execute(therapistId: Int, name: String?, size: Int, companyId: Int?): List<PatientWithRelation> {
        val myPatientIds = appointmentRepository.findPatientIdsByTherapistId(therapistId)

        val patients = if (companyId != null) {
            userRepository.findPatientsByCompanyId(companyId)
                .let { list ->
                    if (name.isNullOrBlank()) list
                    else list.filter { it.name.contains(name, ignoreCase = true) }
                }
                .take(size)
        } else {
            val pageable = PageRequest.of(0, size, Sort.by("name").ascending())
            userRepository.findPatients(name, pageable).content
        }

        return patients
            .map { PatientWithRelation(patient = it, isMyPatient = it.id in myPatientIds) }
            .sortedWith(compareByDescending<PatientWithRelation> { it.isMyPatient }.thenBy { it.patient.name })
    }
}
