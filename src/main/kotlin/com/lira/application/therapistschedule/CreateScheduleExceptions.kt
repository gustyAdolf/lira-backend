package com.lira.application.therapistschedule

import com.lira.domain.appointment.AppointmentRepository
import com.lira.domain.appointment.AppointmentStatus
import com.lira.domain.exceptions.ScheduleException
import com.lira.domain.therapistschedule.TherapistScheduleException
import com.lira.domain.therapistschedule.TherapistScheduleExceptionRepository
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class CreateScheduleExceptions(
    private val repository: TherapistScheduleExceptionRepository,
    private val appointmentRepository: AppointmentRepository,
) {
    fun execute(exceptions: List<TherapistScheduleException>) {
        if (exceptions.isEmpty()) return
        val therapistId = exceptions.first().therapistId

        val hasConflict = exceptions.any { exception ->
            val dayStart = exception.date.atStartOfDay()
            val dayEnd = exception.date.atTime(23, 59, 59)
            appointmentRepository.getTherapistAppointments(therapistId, dayStart, dayEnd, Sort.unsorted())
                .any { it.status != AppointmentStatus.CANCELLED }
        }
        if (hasConflict) throw ScheduleException.TherapistHasAppointmentsThatDay()

        repository.saveAll(exceptions)
    }
}
