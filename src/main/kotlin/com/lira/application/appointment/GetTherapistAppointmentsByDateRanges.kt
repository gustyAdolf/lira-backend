package com.lira.application.appointment

import com.lira.domain.appointment.Appointment
import com.lira.domain.appointment.AppointmentRepository
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
@Transactional(readOnly = true)
class GetTherapistAppointmentsByDateRanges(
    private val appointmentRepository: AppointmentRepository
) {
    fun execute(
        therapistId: Int,
        start: LocalDateTime,
        end: LocalDateTime,
        sort: Sort
    ): List<Appointment> {

        return appointmentRepository.getTherapistAppointments(therapistId, start, end, sort)
    }
}