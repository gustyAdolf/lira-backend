package com.phobos.infrastructure.user

import com.phobos.infrastructure.company.Company
import com.phobos.infrastructure.user.entity.UserEntity
import jakarta.persistence.*

@Entity
@Table(name = "user_company")
data class UserCompany(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    val user: UserEntity,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    val company: Company
)
