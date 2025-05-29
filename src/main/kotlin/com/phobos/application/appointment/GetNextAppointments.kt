package com.phobos.application.appointment

import com.phobos.domain.appointment.Appointment
import com.phobos.domain.appointment.AppointmentRepository
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class GetNextAppointments(
    private val appointmentRepository: AppointmentRepository
) {

    fun execute(therapistId: Int, pageable: Pageable): List<Appointment> {

        return appointmentRepository.findNextAppointmentsForTherapist(therapistId, pageable)
    }
}