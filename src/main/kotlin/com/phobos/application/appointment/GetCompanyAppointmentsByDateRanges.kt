package com.phobos.application.appointment

import com.phobos.domain.appointment.Appointment
import com.phobos.domain.appointment.AppointmentRepository
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
@Transactional(readOnly = true)
class GetCompanyAppointmentsByDateRanges(
    private val appointmentRepository: AppointmentRepository
) {
    fun execute(companyId: Int, start: LocalDateTime, end: LocalDateTime, sort: Sort): List<Appointment> {
        return appointmentRepository.getCompanyAppointments(companyId, start, end, sort)
    }
}