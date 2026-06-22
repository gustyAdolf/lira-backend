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
    val createdAt: LocalDateTime = LocalDateTime.now()
)