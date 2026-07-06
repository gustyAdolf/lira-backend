package com.lira.application.appointment

import com.lira.domain.appointment.Appointment
import com.lira.domain.appointment.AppointmentRepository
import com.lira.domain.appointment.AppointmentStatus
import com.lira.domain.exceptions.ScheduleException
import com.lira.domain.therapistschedule.TherapistScheduleExceptionRepository
import com.lira.domain.therapistschedule.TherapistScheduleRepository
import com.lira.infrastructure.appointment.dto.AppointmentRequest
import com.lira.infrastructure.appointment.dto.toDomain
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalTime

@Service
@Transactional
class CreateAppointment(
    private val appointmentRepository: AppointmentRepository,
    private val scheduleRepository: TherapistScheduleRepository,
    private val exceptionRepository: TherapistScheduleExceptionRepository,
) {
    private val log = LoggerFactory.getLogger(javaClass)

    fun execute(request: AppointmentRequest): Appointment {
        validateSchedule(request)
        val appointment = appointmentRepository.save(request.toDomain())
        log.info(
            "Appointment created: id=${appointment.id}, type=${request.appointmentType}, " +
            "planId=${request.progressPlanId}, patientId=${request.userId}, therapistId=${request.therapistId}"
        )
        return appointment
    }

    private fun validateSchedule(request: AppointmentRequest) {
        val date = request.appointmentDate.toLocalDate()
        val isoDow = date.dayOfWeek.value
        val schedule = scheduleRepository.findByTherapistId(request.therapistId)
            .firstOrNull { it.dayOfWeek == isoDow }
            ?: throw ScheduleException.TherapistNotAvailableDay()

        val newStart = request.appointmentDate.toLocalTime()
        val newEnd = newStart.plusMinutes(request.appointmentDuration.toLong())
        if (newEnd > schedule.endTime) throw ScheduleException.SessionExceedsWorkingHours()

        val dayStart = date.atStartOfDay()
        val dayEnd = date.atTime(23, 59, 59)
        val existing = appointmentRepository.getTherapistAppointments(
            request.therapistId, dayStart, dayEnd, Sort.unsorted()
        )
        val occupiedByAppointments = existing
            .filter { it.status != AppointmentStatus.CANCELLED }
            .map { appt ->
                val apptStart = appt.appointmentDate.toLocalTime()
                apptStart to apptStart.plusMinutes(appt.appointmentDuration.toLong())
            }

        val occupiedByExceptions = exceptionRepository.findByTherapistIdAndDate(request.therapistId, date)
            .map { exception ->
                val start = exception.startTime ?: LocalTime.MIN
                val end = exception.endTime ?: LocalTime.of(23, 59, 59)
                start to end
            }

        val overlaps = (occupiedByAppointments + occupiedByExceptions)
            .any { (occupiedStart, occupiedEnd) -> occupiedStart < newEnd && occupiedEnd > newStart }
        if (overlaps) throw ScheduleException.AppointmentOverlap()
    }
}
