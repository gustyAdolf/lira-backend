package com.phobos.infrastructure.persistence.user

import com.phobos.infrastructure.enums.UserTypeEnum
import jakarta.persistence.*
import java.time.LocalDate


@Entity
@Table(name = "users")
data class User(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int = 0,

    @Column(nullable = false, unique = true)
    val email: String,

    @Column(nullable = false)
    val password: String,

    @Column(nullable = false)
    val name: String,

    @Column(name = "profile_img_path")
    val profileImagePath: String?,

    val birthdate: LocalDate,
    val telephone: String,
    val address: String,

    @Enumerated(EnumType.STRING)
    @Column(name = "user_type", nullable = false)
    val userType: UserTypeEnum,

    @Column(name = "release_date", nullable = false)
    val releaseDate: LocalDate,

    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    var userDisorders: List<UserDisorder> = emptyList(),

    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL], orphanRemoval = true)
    val userCompanies: List<UserCompany> = emptyList(),
)