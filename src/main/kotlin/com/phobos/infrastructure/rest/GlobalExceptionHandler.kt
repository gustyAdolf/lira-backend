package com.phobos.infrastructure.rest

import com.phobos.domain.exceptions.CheckinException
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(CheckinException.NoCheckinExist::class)
    @ResponseStatus(HttpStatus.CONFLICT)
    fun handlerNoExistCheckin(e: CheckinException.NoCheckinExist) =
        ApiResponse<Unit>("error", e.message ?: "No existe registro de checkin para actualizar")

    @ExceptionHandler(CheckinException.AlreadyCheckedInException::class)
    @ResponseStatus(HttpStatus.CONFLICT)
    fun handlerAlreadyCheckedIn(e: CheckinException.AlreadyCheckedInException) =
        ApiResponse<Unit>("error", e.message ?: "Ya existe un check-in abierto")

    @ExceptionHandler(CheckinException.NoOpenCheckinException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handlerNoOpenCheckin(e: CheckinException.NoOpenCheckinException) =
        ApiResponse<Unit>("error", e.message ?: "No hay check-in abierto")

    @ExceptionHandler(Exception::class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    fun handleGeneric(e: Exception) =
        ApiResponse<Unit>("error", "Ha ocurrido un error inesperado")

}