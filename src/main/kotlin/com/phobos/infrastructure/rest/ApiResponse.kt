package com.phobos.infrastructure.rest

data class ApiResponse<T>(
    val status: String,
    val message: String,
    val data: T? = null
)