package com.lira.application.appointment

import com.lira.domain.appointment.AppointmentRepository
import com.lira.domain.appointment.AppointmentStatus
import com.lira.domain.progressplan.PlanSession
import com.lira.domain.progressplan.PlanSessionRepository
import jakarta.transaction.Transactional
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
@Transactional
class CloseAppointmentSession(
    private val appointmentRepository: AppointmentRepository,
    private val planSessionRepository: PlanSessionRepository
) {
    private val log = LoggerFactory.getLogger(javaClass)

    fun execute(appointmentId: Int, therapistNotes: String?) {
        val appointment = appointmentRepository.findById(appointmentId)
        val updated = appointment.copy(
            status = AppointmentStatus.COMPLETED,
            therapistNotes = therapistNotes
        )
        appointmentRepository.update(updated)

        if (appointment.progressPlanId != null) {
            planSessionRepository.save(
                PlanSession(
                    planId = appointment.progressPlanId,
                    therapistId = appointment.therapist.id,
                    appointmentId = appointmentId,
                    notes = therapistNotes
                )
            )
            log.info("PlanSession created for appointment $appointmentId, planId=${appointment.progressPlanId}")
        }

        log.info("Appointment $appointmentId closed as COMPLETED")
    }
}
