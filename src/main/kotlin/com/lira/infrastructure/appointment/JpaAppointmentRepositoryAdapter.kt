package com.lira.infrastructure.appointment

import com.lira.domain.appointment.Appointment
import com.lira.domain.appointment.AppointmentRepository
import com.lira.domain.appointment.AppointmentStatus
import com.lira.domain.exceptions.AppointmentException
import com.lira.infrastructure.user.entity.TherapistEntity
import com.lira.infrastructure.user.entity.PatientEntity
import jakarta.persistence.EntityManager
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
class JpaAppointmentRepositoryAdapter(
    private val jpaAppointmentRepository: JpaAppointmentRepository,
    private val entityManager: EntityManager
) : AppointmentRepository {

    override fun getTherapistAppointments(
        therapistId: Int,
        start: LocalDateTime,
        end: LocalDateTime,
        sort: Sort
    ): List<Appointment> =
        jpaAppointmentRepository.findAppointmentsByTherapistIdAndDateRange(therapistId, start, end, sort)
            .map { it.toDomain() }

    override fun getPatientAppointments(
        patientId: Int,
        start: LocalDateTime,
        end: LocalDateTime,
        sort: Sort
    ): List<Appointment> =
        jpaAppointmentRepository.findPatientAppointmentsWithDateRange(patientId, start, end, sort)
            .map { it.toDomain() }

    override fun getCompanyAppointments(
        companyId: Int,
        start: LocalDateTime,
        end: LocalDateTime,
        sort: Sort
    ): List<Appointment> =
        jpaAppointmentRepository.findCompanyAppointmentWithDateRange(companyId, start, end, sort)
            .map { it.toDomain() }

    override fun findNextAppointmentsForTherapist(therapistId: Int, pageable: Pageable): List<Appointment> {
        val now = LocalDateTime.now()
        return jpaAppointmentRepository.findNextAppointmentsForTherapist(therapistId, now, pageable)
            .map { it.toDomain() }
    }

    override fun findNextAppointmentsForPatient(patientId: Int, size: Int): List<Appointment> =
        jpaAppointmentRepository.findNextAppointmentsForPatient(patientId, PageRequest.of(0, size))
            .map { it.toDomain() }

    override fun findById(id: Int): Appointment =
        jpaAppointmentRepository.findById(id)
            .orElseThrow { AppointmentException.NoAppointmentExists() }
            .toDomain()

    override fun save(appointment: Appointment): Appointment {
        val therapistRef = entityManager.getReference(TherapistEntity::class.java, appointment.therapist.id)
        val patientRef = entityManager.getReference(PatientEntity::class.java, appointment.patient.id)
        val entity = appointment.toEntity(therapistRef, patientRef)
        val saved = jpaAppointmentRepository.save(entity)
        return appointment.copy(id = saved.id)
    }

    override fun update(appointment: Appointment): Appointment {
        val existing = jpaAppointmentRepository.findById(appointment.id)
            .orElseThrow { AppointmentException.NoAppointmentExists() }
        val updated = existing.copy(
            progressPlanId = appointment.progressPlanId,
            appointmentType = appointment.appointmentType,
            therapistNotes = appointment.therapistNotes,
            appointmentDate = appointment.appointmentDate,
            appointmentDuration = appointment.appointmentDuration,
            cost = appointment.cost,
            description = appointment.description,
            status = appointment.status
        )
        jpaAppointmentRepository.save(updated)
        return appointment
    }

    override fun updateAppointmentStatus(appointmentId: Int, status: AppointmentStatus): Appointment {
        val appointment = jpaAppointmentRepository.findById(appointmentId)
            .orElseThrow { AppointmentException.NoAppointmentExists() }
        val updated = appointment.copy(status = status)
        return jpaAppointmentRepository.save(updated).toDomain()
    }

    override fun deleteById(id: Int) {
        jpaAppointmentRepository.deleteById(id)
    }

    override fun findPatientIdsByTherapistId(therapistId: Int): Set<Int> =
        jpaAppointmentRepository.findPatientIdsByTherapistId(therapistId)

    override fun countAppointmentsByTherapists(therapistsId: List<Int>, now: LocalDateTime): Map<Int, Int> =
        jpaAppointmentRepository.countAppointmentsGroupedByTherapist(therapistsId, now).associate { row ->
            val therapistId = (row[0] as Number).toInt()
            val count = (row[1] as Number).toInt()
            therapistId to count
        }

    override fun findCompletedByPlanId(planId: Int): List<Appointment> =
        jpaAppointmentRepository.findCompletedByPlanId(planId).map { it.toDomain() }
}
