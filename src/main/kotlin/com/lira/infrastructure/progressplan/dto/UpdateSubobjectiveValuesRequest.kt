package com.lira.infrastructure.progressplan.dto

data class UpdateSubobjectiveValuesRequest(
    val objectiveId: Int,
    val currentValue: Int? = null,
    val currentSuccess: Int? = null,
    val currentFail: Int? = null
)
