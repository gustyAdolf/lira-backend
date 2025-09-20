package com.phobos.infrastructure.rest

data class ApiResponse<T>(
    val status: ApiResponseStatus,
    val message: String,
    val data: T? = null
)