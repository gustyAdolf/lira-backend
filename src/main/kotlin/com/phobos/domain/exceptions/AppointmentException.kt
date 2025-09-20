package com.phobos.domain.exceptions

sealed class AppointmentException(message: String) : RuntimeException(message) {
    class NoAppointmentExists : AppointmentException("No existe la cita")
}