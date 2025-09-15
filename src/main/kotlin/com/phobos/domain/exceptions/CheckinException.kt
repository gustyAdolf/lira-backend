package com.phobos.domain.exceptions

sealed class CheckinException(message: String) : RuntimeException(message) {
    class NoCheckinExist : CheckinException("No existe registro de check-in")
    class AlreadyCheckedInException : CheckinException("El terapeuta ya tiene un check-in sin cerrar")
    class NoOpenCheckinException : CheckinException("No hay check-in abierto para este terapeuta")
}