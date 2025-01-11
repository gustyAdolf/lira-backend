package com.phobos.infrastructure.mapper

import com.phobos.application.dto.appointment.AppointmentRequest
import com.phobos.application.dto.appointment.AppointmentResponse
import com.phobos.infrastructure.persistence.Appointment
import com.phobos.infrastructure.persistence.MentalDisorder
import com.phobos.infrastructure.persistence.user.User
import org.springframework.stereotype.Component

@Component
class AppointmentMapper {
    companion object {
        fun entityToResponse(appointment: Appointment): AppointmentResponse {
            return AppointmentResponse(
                id = appointment.id,
                appointmentDate = appointment.appointmentDate,
                appointmentDuration = appointment.appointmentDuration,
                userId = appointment.user.id,
                name = appointment.user.name,
                imageUrl = appointment.user.profileImagePath,
                telephone = appointment.user.telephone,
                email = appointment.user.email,
                mentalDisorderId = appointment.mentalDisorder.id,
                mentalDisorder = appointment.mentalDisorder.mentalDisorder
            )
        }

        fun requestToEntity(
            appointmentRequest: AppointmentRequest,
            user: User,
            mentalDisorder: MentalDisorder
        ): Appointment {
            return Appointment(
                therapistId = appointmentRequest.therapistId.toLong(),
                user = user,
                mentalDisorder = mentalDisorder,
                appointmentDate = appointmentRequest.appointmentDate,
                appointmentDuration = appointmentRequest.appointmentDuration,
                description = appointmentRequest.description
            )
        }
    }
}