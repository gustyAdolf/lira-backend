package com.phobos.domain.user

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface UserRepository {
    fun findById(id: Int): User?
    fun findTherapistsByCompanyId(companyId: Int): List<User>
    fun findByEmail(email: String): User?
    fun findUsers(name: String?, mentalDisorder: String?, pageable: Pageable): Page<User>
    fun findAvailabilityByCompanyId(companyId: Int): List<TherapistAvailability>
}