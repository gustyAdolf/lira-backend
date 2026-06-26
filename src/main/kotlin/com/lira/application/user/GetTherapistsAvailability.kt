package com.lira.application.user

import com.lira.domain.appointment.Appointment
import com.lira.domain.appointment.AppointmentRepository
import com.lira.domain.user.TherapistAvailability
import com.lira.domain.user.UserRepository
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
@Transactional(readOnly = true)
class GetTherapistsAvailability(
    private val userRepository: UserRepository,
    private val appointmentRepository: AppointmentRepository
) {

    fun execute(companyId: Int): List<TherapistAvailability> {
        val therapists = userRepository.findTherapistsByCompanyId(companyId)

        if (therapists.isEmpty()) {
            return emptyList()
        }

        val now = LocalDateTime.now()
        val appointmentCounts = appointmentRepository.countAppointmentsByTherapists(
            therapists.map { it.id }, now
        )

        return therapists.map {
            TherapistAvailability(
                therapistId = it.id,
                therapistName = it.name,
                therapistEmail = it.email,
                telephone = it.telephone,
                licenseNumber = it.licenseNumber,
                profileImagePath = it.profileImagePath,
                futureAppointmentsCount = appointmentCounts[it.id] ?: 0,
                nextAppointments = getNextAppointmentsForTherapist(it.id)
            )
        }.sortedBy { it.futureAppointmentsCount }
    }

    private fun getNextAppointmentsForTherapist(therapistId: Int): List<Appointment> {

        val pageable = PageRequest.of(0, 10)
        return appointmentRepository.findNextAppointmentsForTherapist(therapistId, pageable)
    }
}