package com.lira.infrastructure.user.entity

import com.lira.domain.user.*
import com.lira.infrastructure.mentaldisorder.toDomain
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

        // Fallback para proxies Hibernate con herencia JOINED que no inicializan el subtipo
        else -> when (this.userType) {
            UserType.PATIENT -> Patient(
                id = id, email = email, name = name,
                profileImagePath = profileImagePath, password = password,
                telephone = telephone, address = address, releaseDate = releaseDate,
                birthdate = null, gender = null, mentalDisorders = emptyList()
            )
            UserType.THERAPIST -> Therapist(
                id = id, email = email, name = name,
                profileImagePath = profileImagePath, password = password,
                telephone = telephone, address = address, releaseDate = releaseDate
            )
            UserType.COMPANY -> Company(
                id = id, email = email, name = name,
                profileImagePath = profileImagePath, password = password,
                telephone = telephone, address = address, releaseDate = releaseDate,
                cif = null, companyAddress = null
            )
            else -> error("Tipo de usuario no soportado: ${this.userType}")
        }
    }