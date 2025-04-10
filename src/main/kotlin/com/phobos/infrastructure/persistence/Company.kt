package com.phobos.infrastructure.persistence

import com.phobos.infrastructure.persistence.user.UserCompany
import jakarta.persistence.*

@Entity
@Table(name = "company")
data class Company(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int = 0,

    @Column(nullable = false)
    val name: String,
    val email: String? = null,
    val logoImgPath: String? = null,
    val address: String? = null,
    val telephone: String? = null,
    val country: String? = null,

    @OneToMany(mappedBy = "company", cascade = [CascadeType.ALL], orphanRemoval = true)
    val userCompanies: List<UserCompany> = emptyList()
)
