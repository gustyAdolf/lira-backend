package com.phobos.infrastructure.user.entity

import com.phobos.domain.user.*
import com.phobos.infrastructure.mentaldisorder.toDomain
import jakarta.persistence.*
import java.time.LocalDate

@Entity
@Table(name = "users")
@Inheritance(strategy = InheritanceType.JOINED)
open class UserEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    open var id: Int = 0,

    open var email: String = "",
    open var password: String = "",
    open var name: String = "",

    @Column(name = "profile_img_path")
    open var profileImagePath: String? = null,
    open var telephone: String? = null,
    open var address: String? = null,

    @Enumerated(EnumType.STRING)
    @Column(name = "user_type", nullable = false)
    open var userType: UserType = UserType.PATIENT,

    @Column(name = "release_date", nullable = false)
    open var releaseDate: LocalDate = LocalDate.now(),

    )

fun UserEntity.toDomain(): User =
    when (this) {
        is PatientEntity -> Patient(
            id = this.id,
            email = this.email,
            name = this.name,
            profileImagePath = this.profileImagePath,
            password = this.password,
            telephone = this.telephone,
            address = this.address,
            releaseDate = this.releaseDate,
            birthdate = this.birthdate,
            gender = this.gender,
            mentalDisorders = this.userDisorders.map { it.mentalDisorder.toDomain() }
        )

        is TherapistEntity -> Therapist(
            id = this.id,
            email = this.email,
            name = this.name,
            profileImagePath = this.profileImagePath,
            password = this.password,
            telephone = this.telephone,
            address = this.address,
            releaseDate = this.releaseDate,
            licenseNumber = this.licenseNumber
        )

        is CompanyEntity -> Company(
            id = this.id,
            email = this.email,
            name = this.name,
            profileImagePath = this.profileImagePath,
            password = this.password,
            telephone = this.telephone,
            address = this.address,
            releaseDate = this.releaseDate,
            cif = this.cif,
            companyAddress = this.companyAddress
        )

        else -> error("Tipo de usuario no soportado: ${this::class.simpleName}")
    }