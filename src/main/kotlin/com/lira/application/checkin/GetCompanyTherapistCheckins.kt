package com.lira.application.checkin

import com.lira.domain.checkin.CheckinRepository
import com.lira.domain.exceptions.CheckinException
import com.lira.domain.user.UserRepository
import org.springframework.security.access.AccessDeniedException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
import java.time.temporal.ChronoUnit

@Service
@Transactional(readOnly = true)
class GetCompanyTherapistCheckins(
    private val checkinRepository: CheckinRepository,
    private val userRepository: UserRepository,
) {
    fun execute(companyId: Int, therapistId: Int, from: LocalDate?, to: LocalDate?): CompanyCheckinSummary {
        if (from == null || to == null) {
            throw CheckinException.InvalidDateRangeException("Debes indicar un rango de fechas (from y to)")
        }
        if (to.isBefore(from)) {
            throw CheckinException.InvalidDateRangeException("La fecha final debe ser posterior a la inicial")
        }
        val rangeDays = ChronoUnit.DAYS.between(from, to) + 1
        if (rangeDays > MAX_RANGE_DAYS) {
            throw CheckinException.InvalidDateRangeException(
                "El rango de fechas no puede superar los $MAX_RANGE_DAYS días"
            )
        }

        val belongsToCompany = userRepository.findTherapistsByCompanyId(companyId).any { it.id == therapistId }
        if (!belongsToCompany) {
            throw AccessDeniedException("El terapeuta no pertenece a esta clínica")
        }

        val checkins = checkinRepository.getHistory(therapistId, from, to, companyId)
        val totalHours = checkins.sumOf { it.totalHours ?: 0.0 }
        val workedDays = checkins.map { it.checkinTime.toLocalDate() }.distinct().size
        val averageDailyHours = if (workedDays > 0) totalHours / workedDays else 0.0
        val incidentCount = checkins.count { it.autoClosed }

        return CompanyCheckinSummary(
            checkins = checkins,
            totalHours = totalHours,
            averageDailyHours = averageDailyHours,
            incidentCount = incidentCount,
        )
    }

    companion object {
        const val MAX_RANGE_DAYS = 366L
    }
}
