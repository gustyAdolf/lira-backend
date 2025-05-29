package com.phobos.infrastructure.appointment

import com.phobos.domain.appointment.Appointment
import com.phobos.domain.appointment.AppointmentRepository
import com.phobos.domain.appointment.toEntity
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
class JpaAppointmentRepositoryAdapter(
    private val jpaAppointmentRepository: JpaAppointmentRepository
) : AppointmentRepository {

    override fun getAppointments(
        therapistId: Int,
        start: LocalDateTime,
        end: LocalDateTime,
        sort: Sort
    ): List<Appointment> {

        return jpaAppointmentRepository.findAppointmentsByTherapistIdAndDateRange(
            therapistId, start, end, sort
        ).map { it.toDomain() }
    }

    override fun findNextAppointmentsForTherapist(therapistId: Int, pageable: Pageable): List<Appointment> {

        val now = LocalDateTime.now()
        return jpaAppointmentRepository.findNextAppointmentsForTherapist(therapistId, now, pageable)
            .map { it.toDomain() }
    }

    override fun save(appointment: Appointment): Appointment {
        val entity = appointment.toEntity()
        return jpaAppointmentRepository.save(entity).toDomain()
    }

    override fun deleteById(id: Int) {
        jpaAppointmentRepository.deleteById(id.toLong())
    }

    override fun countAppointmentsByTherapists(therapistsId: List<Int>, now: LocalDateTime): Map<Int, Int> {
        return jpaAppointmentRepository.countAppointmentsGroupedByTherapist(therapistsId, now).associate { row ->
            val therapistId = (row[0] as Number).toInt()
            val count = (row[1] as Number).toInt()
            therapistId to count
        }
    }
}