package com.phobos.infrastructure.user.entity

import com.phobos.domain.user.*
import com.phobos.infrastructure.mentaldisorder.toDomain
import com.phobos.infrastructure.user.TherapistEntity
import jakarta.persistence.*
import java.time.LocalDate

@Entity
@Table(name = "users")
class UserEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int = 0,

    val email: String = "",
    val password: String = "",
    val name: String = "",

    @Column(name = "profile_img_path")
    val profileImagePath: String? = null,
    val telephone: String? = null,
    val address: String? = null,

    @Enumerated(EnumType.STRING)
    @Column(name = "user_type", nullable = false)
    val userType: UserType = UserType.PATIENT,

    @Column(name = "release_date", nullable = false)
    val releaseDate: LocalDate = LocalDate.now(),

//    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
//    val userDisorders: List<UserDisorderEntity> = emptyList()
) {
    companion object {
        fun withId(id: Int) = UserEntity(id = id)
    }
}

fun UserEntity.toDomain(
    patientEntity: PatientEntity? = null,
    therapistEntity: TherapistEntity? = null,
    companyEntity: CompanyEntity? = null
): User =
    when (this.userType) {
        UserType.PATIENT -> Patient(
            id = this.id,
            email = this.email,
            name = this.name,
            profileImagePath = this.profileImagePath,
            password = this.password,
            telephone = this.telephone,
            address = this.address,
            releaseDate = this.releaseDate,
            birthdate = patientEntity?.birthdate,
            gender = patientEntity?.gender,
            mentalDisorders = patientEntity?.userDisorders
                ?.map { it.mentalDisorder.toDomain() }
                ?: emptyList()
        )

        UserType.THERAPIST -> Therapist(
            id = this.id,
            email = this.email,
            name = this.name,
            profileImagePath = this.profileImagePath,
            password = this.password,
            telephone = this.telephone,
            address = this.address,
            releaseDate = this.releaseDate,
            licenseNumber = therapistEntity?.licenseNumber
        )

        UserType.COMPANY -> Company(
            id = this.id,
            email = this.email,
            name = this.name,
            profileImagePath = this.profileImagePath,
            password = this.password,
            telephone = this.telephone,
            address = this.address,
            releaseDate = this.releaseDate,
            cif = companyEntity?.cif,
            companyAddress = companyEntity?.address
        )
    }