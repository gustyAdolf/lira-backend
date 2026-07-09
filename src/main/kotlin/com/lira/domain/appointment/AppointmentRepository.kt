package com.lira.domain.appointment

import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import java.time.LocalDateTime

interface AppointmentRepository {

    fun getTherapistAppointments(therapistId: Int, start: LocalDateTime, end: LocalDateTime, sort: Sort): List<Appointment>

    fun getPatientAppointments(patientId: Int, start: LocalDateTime, end: LocalDateTime, sort: Sort): List<Appointment>

    fun getCompanyAppointments(companyId: Int, start: LocalDateTime, end: LocalDateTime, sort: Sort): List<Appointment>

    fun findNextAppointmentsForTherapist(therapistId: Int, pageable: Pageable): List<Appointment>

    fun findNextAppointmentsForPatient(patientId: Int, size: Int): List<Appointment>

    fun findById(id: Int): Appointment

    fun save(appointment: Appointment): Appointment

    fun update(appointment: Appointment): Appointment

    fun updateAppointmentStatus(appointmentId: Int, status: AppointmentStatus): Appointment

    fun confirmAttendance(appointmentId: Int): Appointment

    fun deleteById(id: Int)

    fun countAppointmentsByTherapists(therapistsId: List<Int>, now: LocalDateTime): Map<Int, Int>

    fun findPatientIdsByTherapistId(therapistId: Int): Set<Int>

    fun findCompletedByPlanId(planId: Int): List<Appointment>

    fun findUpcomingActiveAppointments(from: LocalDateTime, to: LocalDateTime): List<Appointment>
}
