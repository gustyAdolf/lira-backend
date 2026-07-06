package com.lira.infrastructure.progressplan.dto

data class CompleteSubobjectiveRequest(
    val objectiveId: Int,
    val completed: Boolean
)
