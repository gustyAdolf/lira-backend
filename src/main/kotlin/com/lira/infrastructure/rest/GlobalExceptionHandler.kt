package com.lira.infrastructure.rest

import com.lira.domain.exceptions.AppointmentException
import com.lira.domain.exceptions.CheckinException
import org.springframework.http.HttpStatus
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(CheckinException.NoCheckinExist::class)
    @ResponseStatus(HttpStatus.CONFLICT)
    fun handlerNoExistCheckin(e: CheckinException.NoCheckinExist) =
        ApiResponse<Unit>(ApiResponseStatus.FAILURE, e.message ?: "No existe registro de checkin para actualizar")

    @ExceptionHandler(CheckinException.AlreadyCheckedInException::class)
    @ResponseStatus(HttpStatus.CONFLICT)
    fun handlerAlreadyCheckedIn(e: CheckinException.AlreadyCheckedInException) =
        ApiResponse<Unit>(ApiResponseStatus.FAILURE, e.message ?: "Ya existe un check-in abierto")

    @ExceptionHandler(CheckinException.NoOpenCheckinException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handlerNoOpenCheckin(e: CheckinException.NoOpenCheckinException) =
        ApiResponse<Unit>(ApiResponseStatus.FAILURE, e.message ?: "No hay check-in abierto")

    @ExceptionHandler(BadCredentialsException::class, UsernameNotFoundException::class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    fun handleAuthError(e: Exception) =
        ApiResponse<Unit>(ApiResponseStatus.FAILURE, "Credenciales incorrectas")

    @ExceptionHandler(AccessDeniedException::class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    fun handleAccessDenied(e: AccessDeniedException) =
        ApiResponse<Unit>(ApiResponseStatus.FAILURE, "Acceso denegado")

    @ExceptionHandler(Exception::class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    fun handleGeneric(e: Exception) =
        ApiResponse<Unit>(ApiResponseStatus.ERROR, "Ha ocurrido un error inesperado")

    @ExceptionHandler(AppointmentException.NoAppointmentExists::class)
    @ResponseStatus(HttpStatus.CONFLICT)
    fun handlerNoAppointmentExist(e: AppointmentException.NoAppointmentExists) =
        ApiResponse<Unit>(ApiResponseStatus.FAILURE, "No existe registro de la cita para actualizar")

}