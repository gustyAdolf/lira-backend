package com.lira.infrastructure.checkin

import com.lira.domain.checkin.Checkin
import com.lira.domain.exceptions.CheckinException
import com.lira.infrastructure.checkin.entity.CheckinEntity
import com.lira.infrastructure.checkin.jpa.JpaCheckinRepository
import com.lira.infrastructure.user.entity.UserEntity
import com.lira.infrastructure.user.jpa.JpaUserRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.Optional

class JpaCheckinRepositoryAdapterTest {

    private lateinit var jpaCheckinRepository: JpaCheckinRepository
    private lateinit var jpaUserRepository: JpaUserRepository
    private lateinit var adapter: JpaCheckinRepositoryAdapter

    private val userId = 42
    private val companyId = 14
    private val user = UserEntity(id = userId)

    @BeforeEach
    fun setUp() {
        jpaCheckinRepository = mockk()
        jpaUserRepository = mockk()
        adapter = JpaCheckinRepositoryAdapter(jpaCheckinRepository, jpaUserRepository)
    }

    // --- checkin() ---

    @Test
    fun `checkin rechaza si ya hay una sesion abierta en la misma clinica`() {
        every { jpaCheckinRepository.findOpenByUserIdAndCompanyId(userId, companyId) } returns
            CheckinEntity(id = 1, user = user, checkinTime = LocalDateTime.now().minusHours(1), companyId = companyId)

        assertThrows<CheckinException.AlreadyCheckedInException> {
            adapter.checkin(Checkin(userId = userId, checkinTime = LocalDateTime.now(), companyId = companyId))
        }
    }

    @Test
    fun `checkin en una clinica no bloquea si la sesion abierta es de otra clinica`() {
        val otherCompanyId = 99
        every { jpaCheckinRepository.findOpenByUserIdAndCompanyId(userId, companyId) } returns null
        every { jpaUserRepository.getReferenceById(userId) } returns user
        every { jpaCheckinRepository.findOverlapping(userId, companyId, any(), any(), any()) } returns emptyList()
        val savedSlot = slot<CheckinEntity>()
        every { jpaCheckinRepository.save(capture(savedSlot)) } answers { savedSlot.captured }

        // El usuario tiene otra sesión abierta en `otherCompanyId`, pero no debe importar aquí.
        adapter.checkin(Checkin(userId = userId, checkinTime = LocalDateTime.now().minusMinutes(5), companyId = companyId))

        verify(exactly = 1) { jpaCheckinRepository.save(any()) }
        verify(exactly = 0) { jpaCheckinRepository.findOpenByUserIdAndCompanyId(userId, otherCompanyId) }
    }

    @Test
    fun `checkin rechaza una hora de entrada en el futuro`() {
        every { jpaCheckinRepository.findOpenByUserIdAndCompanyId(userId, companyId) } returns null

        assertThrows<CheckinException.FutureCheckinException> {
            adapter.checkin(Checkin(userId = userId, checkinTime = LocalDateTime.now().plusHours(1), companyId = companyId))
        }
    }

    @Test
    fun `checkin valido se guarda`() {
        every { jpaCheckinRepository.findOpenByUserIdAndCompanyId(userId, companyId) } returns null
        every { jpaUserRepository.getReferenceById(userId) } returns user
        every { jpaCheckinRepository.findOverlapping(userId, companyId, any(), any(), any()) } returns emptyList()
        val savedSlot = slot<CheckinEntity>()
        every { jpaCheckinRepository.save(capture(savedSlot)) } answers { savedSlot.captured }

        adapter.checkin(Checkin(userId = userId, checkinTime = LocalDateTime.now().minusMinutes(5), companyId = companyId))

        verify(exactly = 1) { jpaCheckinRepository.save(any()) }
        assertEquals(companyId, savedSlot.captured.companyId)
    }

    // --- checkout() ---

    @Test
    fun `checkout rechaza si no hay sesion abierta en esa clinica`() {
        every { jpaCheckinRepository.findOpenByUserIdAndCompanyId(userId, companyId) } returns null

        assertThrows<CheckinException.NoOpenCheckinException> {
            adapter.checkout(userId, companyId, LocalDateTime.now())
        }
    }

    @Test
    fun `checkout rechaza si la hora de salida es anterior a la de entrada`() {
        val checkinTime = LocalDateTime.now().minusHours(1)
        every { jpaCheckinRepository.findOpenByUserIdAndCompanyId(userId, companyId) } returns
            CheckinEntity(id = 1, user = user, checkinTime = checkinTime, companyId = companyId)

        assertThrows<CheckinException.InvalidCheckinRangeException> {
            adapter.checkout(userId, companyId, checkinTime.minusMinutes(1))
        }
    }

    @Test
    fun `checkout rechaza una hora de salida en el futuro`() {
        every { jpaCheckinRepository.findOpenByUserIdAndCompanyId(userId, companyId) } returns
            CheckinEntity(id = 1, user = user, checkinTime = LocalDateTime.now().minusHours(1), companyId = companyId)

        assertThrows<CheckinException.FutureCheckinException> {
            adapter.checkout(userId, companyId, LocalDateTime.now().plusHours(1))
        }
    }

    @Test
    fun `checkout con companyId null cierra la sesion del bucket sin clinica`() {
        every { jpaCheckinRepository.findOpenByUserIdAndCompanyId(userId, null) } returns
            CheckinEntity(id = 1, user = user, checkinTime = LocalDateTime.now().minusHours(1), companyId = null)
        every { jpaCheckinRepository.findOverlapping(userId, null, any(), any(), any()) } returns emptyList()
        every { jpaCheckinRepository.save(any()) } answers { firstArg() }

        adapter.checkout(userId, null, LocalDateTime.now())

        verify(exactly = 1) { jpaCheckinRepository.findOpenByUserIdAndCompanyId(userId, null) }
    }

    // --- updateCheckin() / overlap ---

    @Test
    fun `updateCheckin rechaza si se solapa con otro registro de la misma clinica`() {
        val checkinId = 5
        val existing = CheckinEntity(
            id = checkinId,
            user = user,
            checkinTime = LocalDateTime.now().minusHours(3),
            checkoutTime = LocalDateTime.now().minusHours(2),
            companyId = companyId
        )
        val conflicting = CheckinEntity(
            id = 9,
            user = user,
            checkinTime = LocalDateTime.now().minusHours(4),
            checkoutTime = LocalDateTime.now().minusHours(1),
            companyId = companyId
        )
        every { jpaCheckinRepository.findById(checkinId) } returns Optional.of(existing)
        every {
            jpaCheckinRepository.findOverlapping(userId, companyId, any(), any(), checkinId)
        } returns listOf(conflicting)

        val ex = assertThrows<CheckinException.OverlappingCheckinException> {
            adapter.updateCheckin(
                checkinId,
                LocalDateTime.now().minusHours(3),
                LocalDateTime.now().minusHours(2)
            )
        }
        assertTrue(ex.message!!.contains("#9"))
    }

    @Test
    fun `updateCheckin excluye el propio registro al comprobar solapamiento`() {
        val checkinId = 5
        val checkinTime = LocalDateTime.now().minusHours(2)
        val checkoutTime = LocalDateTime.now().minusHours(1)
        val existing = CheckinEntity(
            id = checkinId, user = user, checkinTime = checkinTime, checkoutTime = checkoutTime,
            companyId = companyId
        )
        every { jpaCheckinRepository.findById(checkinId) } returns Optional.of(existing)
        every {
            jpaCheckinRepository.findOverlapping(userId, companyId, checkinTime, checkoutTime, checkinId)
        } returns emptyList()
        every { jpaCheckinRepository.save(any()) } answers { firstArg() }

        adapter.updateCheckin(checkinId, checkinTime, checkoutTime)

        verify { jpaCheckinRepository.findOverlapping(userId, companyId, checkinTime, checkoutTime, checkinId) }
    }

    // --- createManual() ---

    @Test
    fun `createManual requiere hora de salida`() {
        assertThrows<IllegalArgumentException> {
            adapter.createManual(
                Checkin(userId = userId, checkinTime = LocalDateTime.now().minusHours(2), checkoutTime = null)
            )
        }
    }

    @Test
    fun `createManual rechaza rango invalido`() {
        val checkinTime = LocalDateTime.now().minusHours(1)
        assertThrows<CheckinException.InvalidCheckinRangeException> {
            adapter.createManual(
                Checkin(userId = userId, checkinTime = checkinTime, checkoutTime = checkinTime.minusMinutes(30))
            )
        }
    }

    @Test
    fun `createManual valido se guarda y devuelve el checkin creado`() {
        val checkinTime = LocalDateTime.now().minusHours(3)
        val checkoutTime = LocalDateTime.now().minusHours(1)
        every { jpaUserRepository.getReferenceById(userId) } returns user
        every {
            jpaCheckinRepository.findOverlapping(userId, companyId, checkinTime, checkoutTime, -1)
        } returns emptyList()
        val savedSlot = slot<CheckinEntity>()
        every { jpaCheckinRepository.save(capture(savedSlot)) } answers {
            savedSlot.captured.copy(id = 7)
        }

        val result = adapter.createManual(
            Checkin(userId = userId, checkinTime = checkinTime, checkoutTime = checkoutTime, companyId = companyId)
        )

        assertEquals(7, result.id)
        assertEquals(checkinTime, result.checkinTime)
        assertEquals(checkoutTime, result.checkoutTime)
        assertEquals(companyId, result.companyId)
    }

    // --- getWorkDay() ---

    @Test
    fun `getWorkDay sin companyId devuelve todas las sesiones del dia`() {
        val date = LocalDate.now()
        every { jpaCheckinRepository.findByUserIdAndDate(userId, date) } returns emptyList()

        adapter.getWorkDay(userId, date)

        verify(exactly = 1) { jpaCheckinRepository.findByUserIdAndDate(userId, date) }
    }

    @Test
    fun `getWorkDay con companyId filtra por clinica`() {
        val date = LocalDate.now()
        every {
            jpaCheckinRepository.findByUserIdAndCompanyIdAndDate(userId, companyId, date)
        } returns emptyList()

        adapter.getWorkDay(userId, date, companyId)

        verify(exactly = 1) { jpaCheckinRepository.findByUserIdAndCompanyIdAndDate(userId, companyId, date) }
    }

    // --- getHistory() con rango ---

    @Test
    fun `getHistory sin rango devuelve el historial completo`() {
        every { jpaCheckinRepository.findByUserId(userId) } returns emptyList()

        adapter.getHistory(userId)

        verify(exactly = 1) { jpaCheckinRepository.findByUserId(userId) }
    }

    @Test
    fun `getHistory con rango filtra por fechas`() {
        val from = LocalDate.now().minusDays(30)
        val to = LocalDate.now()
        every { jpaCheckinRepository.findByUserIdAndDateBetween(userId, from, to) } returns emptyList()

        adapter.getHistory(userId, from, to)

        verify(exactly = 1) { jpaCheckinRepository.findByUserIdAndDateBetween(userId, from, to) }
    }

    @Test
    fun `getHistory con rango y companyId filtra tambien por clinica`() {
        val from = LocalDate.now().minusDays(30)
        val to = LocalDate.now()
        every {
            jpaCheckinRepository.findByUserIdAndCompanyIdAndDateBetween(userId, companyId, from, to)
        } returns emptyList()

        adapter.getHistory(userId, from, to, companyId)

        verify(exactly = 1) {
            jpaCheckinRepository.findByUserIdAndCompanyIdAndDateBetween(userId, companyId, from, to)
        }
        verify(exactly = 0) { jpaCheckinRepository.findByUserIdAndDateBetween(any(), any(), any()) }
    }

    // --- cierre automático ---

    @Test
    fun `findOpenFromPreviousDays consulta sesiones abiertas antes de hoy`() {
        every { jpaCheckinRepository.findByCheckoutTimeIsNullAndCheckinTimeBefore(any()) } returns emptyList()

        adapter.findOpenFromPreviousDays()

        verify(exactly = 1) { jpaCheckinRepository.findByCheckoutTimeIsNullAndCheckinTimeBefore(any()) }
    }

    @Test
    fun `autoClose marca el registro como auto_closed`() {
        val checkinId = 3
        val existing = CheckinEntity(id = checkinId, user = user, checkinTime = LocalDateTime.now().minusDays(1))
        every { jpaCheckinRepository.findById(checkinId) } returns Optional.of(existing)
        val savedSlot = slot<CheckinEntity>()
        every { jpaCheckinRepository.save(capture(savedSlot)) } answers { savedSlot.captured }

        adapter.autoClose(checkinId, LocalDateTime.now().minusHours(1))

        assertTrue(savedSlot.captured.autoClosed)
    }
}
