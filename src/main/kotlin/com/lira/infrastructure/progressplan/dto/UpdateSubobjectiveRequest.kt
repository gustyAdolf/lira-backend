package com.lira.infrastructure.progressplan.dto

data class UpdateSubobjectiveRequest(
    val objectiveId: Int,
    val title: String,
    val description: String?,
    val targetValue: Int?,
    val targetSuccess: Int?,
    val targetFail: Int?
)
