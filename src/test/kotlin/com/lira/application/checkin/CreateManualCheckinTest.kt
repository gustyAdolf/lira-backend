package com.lira.application.checkin

import com.lira.domain.checkin.Checkin
import com.lira.domain.checkin.CheckinRepository
import com.lira.infrastructure.checkin.dto.ManualCheckinRequest
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

class CreateManualCheckinTest {

    private lateinit var checkinRepository: CheckinRepository
    private lateinit var createManualCheckin: CreateManualCheckin

    @BeforeEach
    fun setUp() {
        checkinRepository = mockk()
        createManualCheckin = CreateManualCheckin(checkinRepository)
    }

    @Test
    fun `execute delega la creacion manual en el repositorio con los datos del request`() {
        val userId = 7
        val checkinTime = LocalDateTime.now().minusHours(3)
        val checkoutTime = LocalDateTime.now().minusHours(1)
        val request = ManualCheckinRequest(userId = userId, checkinTime = checkinTime, checkoutTime = checkoutTime)
        val created = Checkin(id = 1, userId = userId, checkinTime = checkinTime, checkoutTime = checkoutTime)
        val checkinSlot = slot<Checkin>()
        every { checkinRepository.createManual(capture(checkinSlot)) } returns created

        val result = createManualCheckin.execute(request)

        verify(exactly = 1) { checkinRepository.createManual(any()) }
        assertEquals(userId, checkinSlot.captured.userId)
        assertEquals(checkinTime, checkinSlot.captured.checkinTime)
        assertEquals(checkoutTime, checkinSlot.captured.checkoutTime)
        assertEquals(created, result)
    }
}
