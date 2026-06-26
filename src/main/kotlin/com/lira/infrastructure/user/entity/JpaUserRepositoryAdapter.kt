package com.lira.infrastructure.user.entity

import com.lira.domain.user.*
import com.lira.infrastructure.user.UserCompanyEntity
import com.lira.infrastructure.user.jpa.JpaCompanyRepository
import com.lira.infrastructure.user.jpa.JpaPatientRepository
import com.lira.infrastructure.user.jpa.JpaTherapistRepository
import com.lira.infrastructure.user.jpa.JpaUserRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository

@Repository
class JpaUserRepositoryAdapter(
    private val jpaUserRepository: JpaUserRepository,
    private val jpaPatientRepository: JpaPatientRepository,
    private val jpaTherapistRepository: JpaTherapistRepository,
    private val jpaCompanyRepository: JpaCompanyRepository,
    private val jpaUserCompanyRepository: JpaUserCompanyRepository
) : UserRepository {

    override fun findById(id: Int): User? {
        return jpaUserRepository.findById(id).orElse(null).toDomain()
    }

    override fun findTherapistsByCompanyId(companyId: Int): List<Therapist> {
        return jpaTherapistRepository.findByCompanyId(companyId).map { it.toDomain() }
    }

    override fun findByEmail(email: String): User? {
        val user = jpaUserRepository.findByEmail(email)
        user?.let {
            return it.toDomain()
        }
        throw Exception("Usuario no encontrado") // TODO exception
    }

    override fun findUsers(
        name: String?,
        mentalDisorder: String?,
        userType: UserQueryType,
        pageable: Pageable
    ): Page<User> {
        val result = when {
            name != null && mentalDisorder != null ->
                jpaUserRepository.findByNameContainingIgnoreCaseAndMentalDisorderName(
                    name,
                    mentalDisorder,
                    pageable
                )

            name != null ->
                if (UserQueryType.ALL == userType) jpaUserRepository.findByNameContainingIgnoreCaseAndUserType(
                    name,
                    null,
                    pageable
                )
                else jpaUserRepository.findByNameContainingIgnoreCaseAndUserType(
                    name,
                    userType.toUserType(),
                    pageable
                )

            mentalDisorder != null ->
                jpaUserRepository.findByMentalDisorderName(mentalDisorder, pageable)

            else ->
                if (UserQueryType.ALL == userType) jpaUserRepository.findAll(pageable)
                else jpaUserRepository.findAllByUserType(userType.toUserType(), pageable)


        }
        return result.map { it.toDomain() }
    }

    override fun findPatients(name: String?, pageable: Pageable): Page<Patient> {
        val page = if (name.isNullOrBlank())
            jpaPatientRepository.findAllByOrderByNameAsc(pageable)
        else
            jpaPatientRepository.findByNameContainingIgnoreCaseOrderByNameAsc(name, pageable)
        return page.map { it.toDomain() }
    }

    override fun findAvailabilityByCompanyId(companyId: Int): List<TherapistAvailability> {
        TODO("Not yet implemented")
    }

    override fun findPatientsByCompanyId(companyId: Int): List<Patient> {
        return jpaPatientRepository.findByCompanyId(companyId).map { it.toDomain() }
    }

    override fun saveUser(user: User) {
        when (user) {
            is Patient -> {
                val patient = jpaPatientRepository.save(user.toEntity())
                saveUserCompany(user.companyId, patient)
            }

            is Therapist -> {
                val therapist = jpaTherapistRepository.save(user.toEntity())
                saveUserCompany(user.companyId, therapist)
            }

            is Company -> jpaCompanyRepository.save(user.toEntity())
        }
    }

    override fun updateUser(user: User): User {
        return when (user) {
            is Patient -> {
                val entity = jpaPatientRepository.findById(user.id)
                    .orElseThrow { IllegalArgumentException("Patient not found: ${user.id}") }
                entity.name = user.name
                entity.telephone = user.telephone
                entity.address = user.address
                entity.profileImagePath = user.profileImagePath
                entity.gender = user.gender
                jpaPatientRepository.save(entity).toDomain()
            }
            is Therapist -> {
                val entity = jpaTherapistRepository.findById(user.id)
                    .orElseThrow { IllegalArgumentException("Therapist not found: ${user.id}") }
                entity.name = user.name
                entity.telephone = user.telephone
                entity.address = user.address
                entity.profileImagePath = user.profileImagePath
                entity.licenseNumber = user.licenseNumber
                jpaTherapistRepository.save(entity).toDomain()
            }
            is Company -> {
                val entity = jpaCompanyRepository.findById(user.id)
                    .orElseThrow { IllegalArgumentException("Company not found: ${user.id}") }
                entity.name = user.name
                entity.telephone = user.telephone
                entity.address = user.address
                entity.profileImagePath = user.profileImagePath
                entity.cif = user.cif
                entity.companyAddress = user.companyAddress
                jpaCompanyRepository.save(entity).toDomain()
            }
        }
    }

    override fun updatePassword(userId: Int, hashedPassword: String) {
        val entity = jpaUserRepository.findById(userId)
            .orElseThrow { IllegalArgumentException("User not found: $userId") }
        entity.password = hashedPassword
        jpaUserRepository.save(entity)
    }

    private fun saveUserCompany(companyId: Int, user: UserEntity) {
        val companyRef = jpaCompanyRepository.getReferenceById(companyId)
        jpaUserCompanyRepository.save(
            UserCompanyEntity(
                user = user,
                company = companyRef
            )
        )
    }
}