package com.lira.application.checkin

import com.lira.application.notifications.CreateNotification
import com.lira.domain.checkin.CheckinRepository
import com.lira.domain.notifications.NotificationType
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.time.LocalTime

@Service
@Transactional
class AutoCloseOpenCheckins(
    private val checkinRepository: CheckinRepository,
    private val createNotification: CreateNotification,
) {
    private val log = LoggerFactory.getLogger(javaClass)

    fun execute() {
        val openFromPreviousDays = checkinRepository.findOpenFromPreviousDays()

        openFromPreviousDays.forEach { checkin ->
            val checkinId = checkin.id ?: return@forEach
            val checkinDate = checkin.checkinTime.toLocalDate()
            val endOfDay = LocalDateTime.of(checkinDate, LocalTime.of(23, 59, 59))

            checkinRepository.autoClose(checkinId, endOfDay)
            createNotification.execute(
                recipientUserId = checkin.userId,
                type = NotificationType.WORKDAY_AUTO_CLOSED,
                title = "Jornada cerrada automáticamente",
                body = "Tu jornada del $checkinDate se cerró sola porque no la cerraste a tiempo. " +
                    "Revisa y corrige la hora de salida real.",
            )
            log.info("Auto-closed checkin id=$checkinId userId=${checkin.userId} date=$checkinDate")
        }
    }
}
