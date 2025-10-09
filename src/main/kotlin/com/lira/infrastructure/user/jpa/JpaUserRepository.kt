package com.lira.infrastructure.user.jpa

import com.lira.domain.user.UserType
import com.lira.infrastructure.user.entity.UserEntity
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface JpaUserRepository : JpaRepository<UserEntity, Int> {

    @Query(
        """
        SELECT u from UserEntity u
        WHERE u.userType =:userType
    """
    )
    fun findAllByUserType(userType: UserType, pageable: Pageable): Page<UserEntity>

    fun findByEmail(email: String): UserEntity?

    @Query(
        """
        SELECT u FROM UserEntity u
        WHERE LOWER(u.name) LIKE LOWER(CONCAT('%', :name, '%'))
        AND (:userType IS NULL OR u.userType = :userType)
    """
    )
    fun findByNameContainingIgnoreCaseAndUserType(
        @Param("name") name: String,
        @Param("userType") userType: UserType?,
        pageable: Pageable
    ): Page<UserEntity>

    @Query(
        """
        SELECT p FROM PatientEntity p
        JOIN p.userDisorders pd 
        JOIN pd.mentalDisorder md 
        WHERE LOWER(md.name) LIKE LOWER(CONCAT('%', :disorder, '%'))
    """
    )
    fun findByMentalDisorderName(@Param("disorder") disorder: String, pageable: Pageable): Page<UserEntity>

    @Query(
        """
        SELECT p FROM PatientEntity p 
        JOIN p.userDisorders pd 
        JOIN pd.mentalDisorder md 
        WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%')) 
        AND LOWER(md.name) LIKE LOWER(CONCAT('%', :disorder, '%'))
    """
    )
    fun findByNameContainingIgnoreCaseAndMentalDisorderName(
        @Param("name") name: String,
        @Param("disorder") disorder: String,
        pageable: Pageable
    ): Page<UserEntity>

}
