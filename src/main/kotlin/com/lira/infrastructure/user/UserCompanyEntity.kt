package com.lira.infrastructure.user

import com.lira.infrastructure.user.entity.CompanyEntity
import com.lira.infrastructure.user.entity.UserEntity
import jakarta.persistence.*

@Entity
@Table(name = "user_company")
data class UserCompanyEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    val user: UserEntity,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    val company: CompanyEntity
)
