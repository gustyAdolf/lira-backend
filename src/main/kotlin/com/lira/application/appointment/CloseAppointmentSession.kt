package com.lira.application.appointment

import com.lira.domain.appointment.AppointmentRepository
import com.lira.domain.appointment.AppointmentStatus
import jakarta.transaction.Transactional
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
@Transactional
class CloseAppointmentSession(private val appointmentRepository: AppointmentRepository) {
    private val log = LoggerFactory.getLogger(javaClass)

    fun execute(appointmentId: Int, therapistNotes: String?) {
        val appointment = appointmentRepository.findById(appointmentId)
        val updated = appointment.copy(
            status = AppointmentStatus.COMPLETED,
            therapistNotes = therapistNotes
        )
        appointmentRepository.update(updated)
        log.info("Appointment $appointmentId closed as COMPLETED")
    }
}
