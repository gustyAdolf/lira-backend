package com.lira.application.therapistschedule

import com.lira.domain.appointment.AppointmentRepository
import com.lira.domain.appointment.AppointmentStatus
import com.lira.domain.therapistschedule.TherapistSchedule
import com.lira.domain.therapistschedule.TherapistScheduleRepository
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.LocalTime

@Service
class TherapistScheduleService(
    private val scheduleRepository: TherapistScheduleRepository,
    private val appointmentRepository: AppointmentRepository,
) {
    fun getSchedule(therapistId: Int): List<TherapistSchedule> =
        scheduleRepository.findByTherapistId(therapistId)

    fun replaceSchedule(therapistId: Int, days: List<TherapistSchedule>) =
        scheduleRepository.replaceAll(therapistId, days)

    fun getAvailableSlots(therapistId: Int, date: LocalDate, durationMinutes: Int): List<LocalTime> {
        val isoDow = date.dayOfWeek.value
        val schedule = scheduleRepository.findByTherapistId(therapistId)
            .firstOrNull { it.dayOfWeek == isoDow }
            ?: return emptyList()

        val dayStart = date.atStartOfDay()
        val dayEnd = date.atTime(23, 59, 59)
        val existingAppointments = appointmentRepository.getTherapistAppointments(
            therapistId, dayStart, dayEnd,
            org.springframework.data.domain.Sort.unsorted()
        )

        val activeAppointments = existingAppointments.filter { it.status != AppointmentStatus.CANCELLED }

        // Align first slot to the nearest :00 or :30 >= schedule.startTime
        val startMinute = schedule.startTime.hour * 60 + schedule.startTime.minute
        val alignedMinute = if (startMinute % 30 == 0) startMinute else ((startMinute / 30) + 1) * 30
        var current = LocalTime.of(alignedMinute / 60, alignedMinute % 60)

        val latestStart = schedule.endTime.minusMinutes(durationMinutes.toLong())
        val slots = mutableListOf<LocalTime>()

        while (!current.isAfter(latestStart)) {
            val slotEnd = current.plusMinutes(durationMinutes.toLong())
            val overlaps = activeAppointments.any { appt ->
                val apptStart = appt.appointmentDate.toLocalTime()
                val apptEnd = apptStart.plusMinutes(appt.appointmentDuration.toLong())
                apptStart < slotEnd && apptEnd > current
            }
            if (!overlaps) slots.add(current)
            current = current.plusMinutes(30)
        }

        return slots
    }
}
