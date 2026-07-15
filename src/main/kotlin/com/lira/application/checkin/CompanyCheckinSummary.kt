package com.lira.application.checkin

import com.lira.domain.checkin.Checkin

data class CompanyCheckinSummary(
    val checkins: List<Checkin>,
    val totalHours: Double,
    val averageDailyHours: Double,
    val incidentCount: Int,
)
