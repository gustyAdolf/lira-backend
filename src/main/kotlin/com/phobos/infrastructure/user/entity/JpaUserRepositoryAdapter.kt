package com.phobos.infrastructure.user.entity

import com.phobos.domain.user.*
import com.phobos.infrastructure.user.jpa.JpaCompanyRepository
import com.phobos.infrastructure.user.jpa.JpaPatientRepository
import com.phobos.infrastructure.user.jpa.JpaTherapistRepository
import com.phobos.infrastructure.user.jpa.JpaUserRepository
import com.phobos.infrastructure.user.toDomain
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository

@Repository
class JpaUserRepositoryAdapter(
    private val jpaUserRepository: JpaUserRepository,
    private val jpaPatientRepository: JpaPatientRepository,
    private val jpaTherapistRepository: JpaTherapistRepository,
    private val jpaCompanyRepository: JpaCompanyRepository
) : UserRepository {

    override fun findById(id: Int): User? {
        val userEntity = jpaUserRepository.findById(id).orElse(null) ?: return null
        return convertToUserDomain(userEntity)
    }

    override fun findTherapistsByCompanyId(companyId: Int): List<Therapist> {
        return TODO()//jpaUserRepository.findTherapistsByCompanyId(companyId)
        //.map { it.toDomain() }
    }

    override fun findByEmail(email: String): User? {
        val user = jpaUserRepository.findByEmail(email)
        user?.let {
            return convertToUserDomain(it)
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

    override fun savePatient(patient: Patient): Patient {
        return jpaPatientRepository.save(patient.toEntity()).toDomain()
    }

    override fun saveTherapist(therapist: Therapist): Therapist {
        return jpaTherapistRepository.save(therapist.toEntity()).toDomain()
    }


    private fun convertToUserDomain(userEntity: UserEntity): User {
        return when (userEntity.userType) {
            UserType.PATIENT -> {
                val patientEntity = jpaPatientRepository.findById(userEntity.id).orElse(null)
                return userEntity.toDomain(patientEntity = patientEntity)
            }

            UserType.THERAPIST -> {
                val therapistEntity = jpaTherapistRepository.findById(userEntity.id).orElse(null)
                return userEntity.toDomain(therapistEntity = therapistEntity)
            }

            UserType.COMPANY -> {
                val companyEntity = jpaCompanyRepository.findById(userEntity.id).orElse(null)
                return userEntity.toDomain(companyEntity = companyEntity)
            }
        }
    }
}