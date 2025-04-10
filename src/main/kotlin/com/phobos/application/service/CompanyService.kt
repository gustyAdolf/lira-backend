package com.phobos.application.service

import com.phobos.application.repository.UserCompanyRepository
import com.phobos.infrastructure.persistence.user.User
import org.springframework.stereotype.Service

@Service
class CompanyService(
    private val userCompanyRepository: UserCompanyRepository
) {
    fun getUsersByCompany(companyId: Int, userType: String? = null): List<User> {
        return userCompanyRepository.findUsersByCompanyAndUserType(companyId, userType)
    }
}