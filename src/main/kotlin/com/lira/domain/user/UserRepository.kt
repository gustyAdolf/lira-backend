package com.lira.domain.user

import com.lira.domain.user.UserQueryType
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface UserRepository {
    fun findById(id: Int): User?
    fun findTherapistsByCompanyId(companyId: Int): List<Therapist>
    fun findByEmail(email: String): User?
    fun findUsers(name: String?, mentalDisorder: String?, userType: UserQueryType, pageable: Pageable): Page<User>
    fun findPatients(name: String?, pageable: Pageable): Page<Patient>
    fun findAvailabilityByCompanyId(companyId: Int): List<TherapistAvailability>
    fun saveUser(user: User)
}