package com.lira.domain.exceptions

sealed class AppointmentException(message: String) : RuntimeException(message) {
    class NoAppointmentExists : AppointmentException("No existe la cita")
    class NotEditable : AppointmentException("La cita no se puede editar en su estado actual")
}