package com.lira.infrastructure.scheduling

import com.lira.application.appointment.ProcessAppointmentReconfirmations
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class AppointmentReminderScheduler(
    private val processAppointmentReconfirmations: ProcessAppointmentReconfirmations,
) {
    private val log = LoggerFactory.getLogger(javaClass)

    @Scheduled(fixedDelay = SWEEP_INTERVAL_MS)
    fun sweep() {
        try {
            processAppointmentReconfirmations.execute()
        } catch (e: Exception) {
            log.error("Appointment reconfirmation sweep failed: ${e.message}", e)
        }
    }

    companion object {
        private const val SWEEP_INTERVAL_MS = 15 * 60 * 1000L
    }
}
