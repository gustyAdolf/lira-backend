package com.lira.infrastructure.checkin.dto

import com.lira.application.checkin.CompanyCheckinSummary

data class CompanyCheckinSummaryResponse(
    val checkins: List<CheckinResponse>,
    val totalHours: Double,
    val averageDailyHours: Double,
    val incidentCount: Int,
)

fun CompanyCheckinSummary.toResponse() = CompanyCheckinSummaryResponse(
    checkins = checkins.map { it.toResponse() },
    totalHours = totalHours,
    averageDailyHours = averageDailyHours,
    incidentCount = incidentCount,
)
