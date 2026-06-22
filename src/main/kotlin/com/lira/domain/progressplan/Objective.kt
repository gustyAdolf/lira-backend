package com.lira.domain.progressplan

import java.time.LocalDateTime

data class Objective(
    val id: Int = 0,
    val title: String,
    val description: String?,
    val orderIndex: Int,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val subobjectives: List<Subobjective>
)
