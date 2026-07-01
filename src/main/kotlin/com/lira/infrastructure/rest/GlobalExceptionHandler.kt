package com.lira.infrastructure.rest

import com.lira.domain.exceptions.AppointmentException
import com.lira.domain.exceptions.CheckinException
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {

    private val log = LoggerFactory.getLogger(javaClass)

    @ExceptionHandler(CheckinException.NoCheckinExist::class)
    @ResponseStatus(HttpStatus.CONFLICT)
    fun handlerNoExistCheckin(e: CheckinException.NoCheckinExist): ApiResponse<Unit> {
        log.warn("Checkin conflict: ${e.message}")
        return ApiResponse(ApiResponseStatus.FAILURE, e.message ?: "No existe registro de checkin para actualizar")
    }

    @ExceptionHandler(CheckinException.AlreadyCheckedInException::class)
    @ResponseStatus(HttpStatus.CONFLICT)
    fun handlerAlreadyCheckedIn(e: CheckinException.AlreadyCheckedInException): ApiResponse<Unit> {
        log.warn("Checkin conflict: ${e.message}")
        return ApiResponse(ApiResponseStatus.FAILURE, e.message ?: "Ya existe un check-in abierto")
    }

    @ExceptionHandler(CheckinException.NoOpenCheckinException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handlerNoOpenCheckin(e: CheckinException.NoOpenCheckinException): ApiResponse<Unit> {
        log.warn("Checkout rejected: ${e.message}")
        return ApiResponse(ApiResponseStatus.FAILURE, e.message ?: "No hay check-in abierto")
    }

    @ExceptionHandler(BadCredentialsException::class, UsernameNotFoundException::class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    fun handleAuthError(e: Exception): ApiResponse<Unit> {
        log.warn("Authentication failed: ${e.message}")
        return ApiResponse(ApiResponseStatus.FAILURE, "Credenciales incorrectas")
    }

    @ExceptionHandler(AccessDeniedException::class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    fun handleAccessDenied(e: AccessDeniedException): ApiResponse<Unit> {
        log.warn("Access denied: ${e.message}")
        return ApiResponse(ApiResponseStatus.FAILURE, "Acceso denegado")
    }

    @ExceptionHandler(AppointmentException.NoAppointmentExists::class)
    @ResponseStatus(HttpStatus.CONFLICT)
    fun handlerNoAppointmentExist(e: AppointmentException.NoAppointmentExists): ApiResponse<Unit> {
        log.warn("Appointment not found: ${e.message}")
        return ApiResponse(ApiResponseStatus.FAILURE, "No existe registro de la cita para actualizar")
    }

    @ExceptionHandler(AppointmentException.NotEditable::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handlerAppointmentNotEditable(e: AppointmentException.NotEditable): ApiResponse<Unit> {
        log.warn("Appointment not editable: ${e.message}")
        return ApiResponse(ApiResponseStatus.FAILURE, e.message ?: "La cita no se puede editar en su estado actual")
    }

    @ExceptionHandler(NoSuchElementException::class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    fun handleNotFound(e: NoSuchElementException): ApiResponse<Unit> {
        log.warn("Resource not found: ${e.message}")
        return ApiResponse(ApiResponseStatus.FAILURE, e.message ?: "Recurso no encontrado")
    }

    @ExceptionHandler(Exception::class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    fun handleGeneric(e: Exception): ApiResponse<Unit> {
        log.error("Unexpected error: ${e.javaClass.simpleName} — ${e.message}", e)
        return ApiResponse(ApiResponseStatus.ERROR, "Ha ocurrido un error inesperado")
    }
}