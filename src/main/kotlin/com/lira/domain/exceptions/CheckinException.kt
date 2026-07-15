package com.lira.domain.exceptions

sealed class CheckinException(message: String) : RuntimeException(message) {
    class NoCheckinExist : CheckinException("No existe registro de check-in")
    class AlreadyCheckedInException : CheckinException("El terapeuta ya tiene un check-in sin cerrar")
    class NoOpenCheckinException : CheckinException("No hay check-in abierto para este terapeuta")
    class InvalidCheckinRangeException :
        CheckinException("La hora de salida debe ser posterior a la hora de entrada")
    class FutureCheckinException : CheckinException("No se puede fichar en el futuro")
    class OverlappingCheckinException(conflictingCheckinId: Int, rangeDescription: String) :
        CheckinException("Se solapa con el registro #$conflictingCheckinId ($rangeDescription)")
    class InvalidDateRangeException(message: String) : CheckinException(message)
}