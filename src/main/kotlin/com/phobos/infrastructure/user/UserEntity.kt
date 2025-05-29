package com.phobos.infrastructure.user

import com.phobos.domain.mentaldisorder.MentalDisorder
import com.phobos.domain.user.User
import com.phobos.domain.user.UserType
import jakarta.persistence.*
import org.hibernate.Hibernate
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

    val birthdate: LocalDate? = null,
    val telephone: String? = null,
    val address: String? = null,

    @Enumerated(EnumType.STRING)
    @Column(name = "user_type", nullable = false)
    val userType: UserType = UserType.PATIENT,

    @Column(name = "release_date", nullable = false)
    val releaseDate: LocalDate = LocalDate.now(),

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    val userDisorders: List<UserDisorderEntity> = emptyList()
) {
    companion object {
        fun withId(id: Int) = UserEntity(id = id)
    }
}

fun UserEntity.toDomain(): User {
    return User(
        id = this.id,
        name = this.name,
        email = this.email,
        password = this.password,
        userType = this.userType,
        birthdate = this.birthdate,
        telephone = this.telephone,
        address = this.address,
        imageUrl = this.profileImagePath ?: "",
        mentalDisorders = if (Hibernate.isInitialized(this.userDisorders)) {
            this.userDisorders.map {
                MentalDisorder(
                    id = it.mentalDisorder.id,
                    name = it.mentalDisorder.name
                )
            }
        } else {
            emptyList()
        }
    )
}