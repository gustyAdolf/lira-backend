package com.lira.domain.exceptions

sealed class AppointmentException(message: String) : RuntimeException(message) {
    class NoAppointmentExists : AppointmentException("No existe la cita")
    class NotEditable : AppointmentException("La cita no se puede editar en su estado actual")
    class CancellationWindowExpired : AppointmentException(
        "Ya no puedes cancelar esta cita directamente. Puedes solicitar la cancelación para que el terapeuta la revise."
    )
}