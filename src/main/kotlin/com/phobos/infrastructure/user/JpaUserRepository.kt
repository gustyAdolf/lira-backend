package com.phobos.infrastructure.user

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.util.*

interface JpaUserRepository : JpaRepository<UserEntity, Int> {

    @Query(
        """
        SELECT u FROM UserEntity u
        JOIN UserCompany uc ON u.id = uc.user.id
        WHERE uc.company.id = :companyId
          AND u.userType = 'THERAPIST'
    """
    )
    fun findTherapistsByCompanyId(@Param("companyId") companyId: Int): List<UserEntity>

    fun findByEmail(email: String): UserEntity?

    fun findByNameContainingIgnoreCase(name: String, pageable: Pageable): Page<UserEntity>

    @Query(
        """
        SELECT u FROM UserEntity u 
        JOIN u.userDisorders ud 
        JOIN ud.mentalDisorder md 
        WHERE LOWER(md.name) LIKE LOWER(CONCAT('%', :disorder, '%'))
    """
    )
    fun findByMentalDisorderName(@Param("disorder") disorder: String, pageable: Pageable): Page<UserEntity>

    @Query(
        """
        SELECT u FROM UserEntity u 
        JOIN u.userDisorders ud 
        JOIN ud.mentalDisorder md 
        WHERE LOWER(u.name) LIKE LOWER(CONCAT('%', :name, '%')) 
        AND LOWER(md.name) LIKE LOWER(CONCAT('%', :disorder, '%'))
    """
    )
    fun findByNameContainingIgnoreCaseAndMentalDisorderName(
        @Param("name") name: String,
        @Param("disorder") disorder: String,
        pageable: Pageable
    ): Page<UserEntity>

    @EntityGraph(attributePaths = ["userDisorders", "userDisorders.mentalDisorder"])
    fun findWithDisordersById(id: Int): Optional<UserEntity>
}
