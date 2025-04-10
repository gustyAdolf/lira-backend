package com.phobos.infrastructure.persistence.user

import com.phobos.infrastructure.persistence.Company
import jakarta.persistence.*

@Entity
@Table(name = "user_company")
data class UserCompany(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    val user: User,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    val company: Company
)
