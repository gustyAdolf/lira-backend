package com.phobos.application.repository

import com.phobos.infrastructure.persistence.user.User
import com.phobos.infrastructure.persistence.user.UserCompany
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface UserCompanyRepository : JpaRepository<UserCompany, Int> {
    @Query(
        """
        SELECT uc.user 
        FROM UserCompany uc
        WHERE uc.company.id = :companyId
            AND (:userType IS NULL OR uc.user.userType = :userType)
        """
    )
    fun findUsersByCompanyAndUserType(
        @Param("companyId") companyId: Int,
        @Param("userType") userType: String?,
    ): List<User>
}