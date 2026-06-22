package com.lira.application.user

import com.lira.domain.user.UserRepository
import com.lira.infrastructure.user.dto.PatientRequest
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.LocalDate
import com.lira.domain.user.User

class CreateUserTest {

    private lateinit var userRepository: UserRepository
    private lateinit var createUser: CreateUser

    @BeforeEach
    fun setUp() {
        userRepository = mockk()
        createUser = CreateUser(
            userRepository = userRepository,
            imagePath = "/tmp/lira-test-images/",
            preffixImageName = "/images/users/"
        )
        justRun { userRepository.saveUser(any()) }
    }

    @Test
    fun `execute hashea la contraseña antes de persistir`() {
        // given
        val rawPassword = "temporal"
        val request = PatientRequest(
            name = "Test Paciente",
            email = "test@lira.com",
            password = rawPassword,
            address = "Calle Test 1",
            id = 0,
            companyId = 14,
            profileImagePath = null,
            telephone = "600000000",
            birthdate = LocalDate.of(1990, 1, 1),
            gender = "male"
        )
        val userSlot = slot<User>()
        justRun { userRepository.saveUser(capture(userSlot)) }

        // when
        createUser.execute(request, null)

        // then
        val savedUser = userSlot.captured
        assertNotEquals(rawPassword, savedUser.password)
        // BCrypt hash siempre empieza por $2a$
        assert(savedUser.password.startsWith("\$2a\$")) {
            "La contraseña debe estar hasheada con BCrypt"
        }
    }

    @Test
    fun `execute llama a saveUser exactamente una vez cuando no hay imagen`() {
        // given
        val request = PatientRequest(
            name = "Test Paciente",
            email = "test2@lira.com",
            password = "temporal",
            address = "Calle Test 2",
            id = 0,
            companyId = 14,
            profileImagePath = null,
            telephone = "600000001",
            birthdate = LocalDate.of(1995, 5, 15),
            gender = "female"
        )

        // when
        createUser.execute(request, null)

        // then
        verify(exactly = 1) { userRepository.saveUser(any()) }
    }
}
