package com.phobos.application.service

import com.phobos.application.dto.appointment.AppointmentRequest
import com.phobos.application.dto.appointment.AppointmentResponse
import com.phobos.application.repository.AppointmentRepository
import com.phobos.application.repository.MentalDisorderRepository
import com.phobos.application.repository.UserRepository
import com.phobos.infrastructure.mapper.AppointmentMapper
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
class AppointmentService(
    private val appointmentRepository: AppointmentRepository,
    private val userRepository: UserRepository,
    private val mentalDisorderRepository: MentalDisorderRepository
) {
    @Transactional(readOnly = true)
    fun getAppointments(
        therapistId: Int,
        startDate: LocalDateTime,
        endDate: LocalDateTime,
        sortBy: String,
        sortDirection: Sort.Direction
    ): List<AppointmentResponse> {
        val sort = Sort.by(sortDirection, sortBy)
        return appointmentRepository.findAppointmentsByTherapistIdAndDateRange(therapistId, startDate, endDate, sort)
            .map {
                AppointmentMapper.entityToResponse(it);
            }
    }

    @Transactional
    fun createAppointment(appointment: AppointmentRequest): AppointmentResponse {
        val user = userRepository.findById(appointment.userId).orElseThrow()
        val mentalDisorder = mentalDisorderRepository.findById(appointment.mentalDisorderId).orElseThrow()
        val appointmentToSave = AppointmentMapper.requestToEntity(
            appointmentRequest = appointment,
            user = user,
            mentalDisorder = mentalDisorder
        )
        val savedAppointment = appointmentRepository.save(appointmentToSave)
        return AppointmentMapper.entityToResponse(savedAppointment)
    }

    @Transactional
    fun deleteAppointment(appointmentId: Int) {
        appointmentRepository.deleteById(appointmentId)
    }
}