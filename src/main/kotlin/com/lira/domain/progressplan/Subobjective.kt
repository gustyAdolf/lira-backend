package com.lira.domain.progressplan

import java.time.LocalDateTime

data class Subobjective(
    var id: Int = 0,
    val title: String,
    val description: String?,
    val type: SubobjectiveType,
    val targetValue: Int?,
    val targetSuccess: Int?,
    val targetFail: Int?,
    var currentProgress: Double = 0.0,
    var currentValue: Int = 0,
    var currentSuccess: Int = 0,
    var currentFail: Int = 0,
    var isCompleted: Boolean = false,
    val createdAt: LocalDateTime = LocalDateTime.now()
)