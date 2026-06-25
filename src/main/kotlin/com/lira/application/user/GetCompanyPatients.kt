package com.lira.application.user

import com.lira.domain.user.Patient
import com.lira.domain.user.UserRepository
import org.springframework.stereotype.Service

@Service
class GetCompanyPatients(private val userRepository: UserRepository) {
    fun execute(companyId: Int): List<Patient> =
        userRepository.findPatientsByCompanyId(companyId)
}
