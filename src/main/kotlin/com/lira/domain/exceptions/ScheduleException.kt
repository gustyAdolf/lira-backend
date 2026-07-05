package com.lira.domain.exceptions

sealed class ScheduleException(message: String) : RuntimeException(message) {
    class TherapistNotAvailableDay : ScheduleException("El terapeuta no trabaja ese día")
    class SessionExceedsWorkingHours : ScheduleException("La sesión terminaría fuera del horario del terapeuta")
    class AppointmentOverlap : ScheduleException("El terapeuta ya tiene una cita en ese horario")
}
