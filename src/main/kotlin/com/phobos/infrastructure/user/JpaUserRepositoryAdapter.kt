package com.phobos.infrastructure.user

import com.phobos.domain.user.TherapistAvailability
import com.phobos.domain.user.User
import com.phobos.domain.user.UserRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository

@Repository
class JpaUserRepositoryAdapter(
    private val jpaUserRepository: JpaUserRepository
) : UserRepository {

    override fun findById(id: Int): User? {
        return jpaUserRepository.findWithDisordersById(id).map {
            it.toDomain()
        }.orElse(null)
    }

    override fun findTherapistsByCompanyId(companyId: Int): List<User> {
        return jpaUserRepository.findTherapistsByCompanyId(companyId)
            .map { it.toDomain() }
    }

    override fun findByEmail(email: String): User? {
        val user = jpaUserRepository.findByEmail(email)
        user?.let {
            return it.toDomain()
        }
        throw Exception("Usuario no encontrado") // TODO exception
    }

    override fun findUsers(name: String?, mentalDisorder: String?, pageable: Pageable): Page<User> {
        val result = when {
            name != null && mentalDisorder != null ->
                jpaUserRepository.findByNameContainingIgnoreCaseAndMentalDisorderName(name, mentalDisorder, pageable)

            name != null ->
                jpaUserRepository.findByNameContainingIgnoreCase(name, pageable)

            mentalDisorder != null ->
                jpaUserRepository.findByMentalDisorderName(mentalDisorder, pageable)

            else ->
                jpaUserRepository.findAll(pageable)
        }
        return result.map { it.toDomain() }
    }

    override fun findAvailabilityByCompanyId(companyId: Int): List<TherapistAvailability> {
        TODO("Not yet implemented")
    }


}