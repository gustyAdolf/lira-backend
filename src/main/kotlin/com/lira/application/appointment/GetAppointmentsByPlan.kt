package com.lira.application.appointment

import com.lira.domain.appointment.Appointment
import com.lira.domain.appointment.AppointmentRepository
import org.springframework.stereotype.Service

@Service
class GetAppointmentsByPlan(private val appointmentRepository: AppointmentRepository) {
    fun execute(planId: Int): List<Appointment> =
        appointmentRepository.findCompletedByPlanId(planId)
}
