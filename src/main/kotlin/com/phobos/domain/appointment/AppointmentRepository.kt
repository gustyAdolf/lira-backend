package com.phobos.domain.appointment

import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import java.time.LocalDateTime

interface AppointmentRepository {

    fun getAppointments(
        therapistId: Int,
        start: LocalDateTime,
        end: LocalDateTime,
        sort: Sort
    ): List<Appointment>

    fun findNextAppointmentsForTherapist(therapistId: Int, pageable: Pageable): List<Appointment>

    fun save(appointment: Appointment): Appointment

    fun deleteById(id: Int)

    fun countAppointmentsByTherapists(therapistsId: List<Int>, now: LocalDateTime): Map<Int, Int>
}