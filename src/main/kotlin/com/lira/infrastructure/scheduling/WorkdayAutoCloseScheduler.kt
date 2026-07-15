package com.lira.infrastructure.scheduling

import com.lira.application.checkin.AutoCloseOpenCheckins
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class WorkdayAutoCloseScheduler(
    private val autoCloseOpenCheckins: AutoCloseOpenCheckins,
) {
    private val log = LoggerFactory.getLogger(javaClass)

    @Scheduled(cron = "0 0 0 * * *")
    fun sweep() {
        try {
            autoCloseOpenCheckins.execute()
        } catch (e: Exception) {
            log.error("Workday auto-close sweep failed: ${e.message}", e)
        }
    }
}
