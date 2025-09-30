package com.phobos.infrastructure.appointment

import com.phobos.domain.appointment.Appointment
import com.phobos.domain.appointment.AppointmentRepository
import com.phobos.domain.appointment.toEntity
import com.phobos.domain.exceptions.AppointmentException
import com.phobos.infrastructure.mentaldisorder.MentalDisorderEntity
import com.phobos.infrastructure.user.entity.TherapistEntity
import com.phobos.infrastructure.user.entity.PatientEntity
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
    ): List<Appointment> {

        return jpaAppointmentRepository.findAppointmentsByTherapistIdAndDateRange(
            therapistId, start, end, sort
        ).map { it.toDomain() }
    }

    override fun getPatientAppointments(
        patientId: Int,
        start: LocalDateTime,
        end: LocalDateTime,
        sort: Sort
    ): List<Appointment> {

        return jpaAppointmentRepository.findPatientAppointmentsWithDateRange(
            patientId, start, end, sort
        ).map { it.toDomain() }
    }

    override fun getCompanyAppointments(
        companyId: Int,
        start: LocalDateTime,
        end: LocalDateTime,
        sort: Sort
    ): List<Appointment> {
        return jpaAppointmentRepository.findCompanyAppointmentWithDateRange(
            companyId, start, end, sort
        ).map { it.toDomain() }
    }

    override fun findNextAppointmentsForTherapist(therapistId: Int, pageable: Pageable): List<Appointment> {

        val now = LocalDateTime.now()
        return jpaAppointmentRepository.findNextAppointmentsForTherapist(therapistId, now, pageable)
            .map { it.toDomain() }
    }

    override fun findNextAppointmentsForPatient(patientId: Int, size: Int): List<Appointment> {

        return jpaAppointmentRepository.findNextAppointmentsForPatient(patientId, PageRequest.of(0, size))
            .map { it.toDomain() }
    }

    override fun save(appointment: Appointment): Appointment {
        val therapistRef = entityManager.getReference(TherapistEntity::class.java, appointment.therapist.id)
        val patientRef = entityManager.getReference(PatientEntity::class.java, appointment.patient.id)
        val mentalDisorderRef =
            entityManager.getReference(MentalDisorderEntity::class.java, appointment.mentalDisorder.id)

        val entity = appointment.toEntity(
            therapistRef,
            patientRef,
            mentalDisorderRef
        )

        return jpaAppointmentRepository.save(entity).toDomain()
    }

    override fun updateAppointmentStatus(
        appointmentId: Int,
        status: AppointmentStatus
    ): Appointment {
        val appointment =
            jpaAppointmentRepository.findById(appointmentId).orElseThrow { AppointmentException.NoAppointmentExists() }
        val updatedAppointment = appointment.copy(status = status)
        return jpaAppointmentRepository.save(updatedAppointment).toDomain()
    }

    override fun deleteById(id: Int) {
        jpaAppointmentRepository.deleteById(id)
    }

    override fun countAppointmentsByTherapists(therapistsId: List<Int>, now: LocalDateTime): Map<Int, Int> {
        return jpaAppointmentRepository.countAppointmentsGroupedByTherapist(therapistsId, now).associate { row ->
            val therapistId = (row[0] as Number).toInt()
            val count = (row[1] as Number).toInt()
            therapistId to count
        }
    }
}