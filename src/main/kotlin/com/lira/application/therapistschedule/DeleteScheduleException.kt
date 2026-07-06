package com.lira.application.therapistschedule

import com.lira.domain.therapistschedule.TherapistScheduleExceptionRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

@Service
@Transactional
class DeleteScheduleException(
    private val repository: TherapistScheduleExceptionRepository,
) {
    fun execute(therapistId: Int, date: LocalDate) = repository.deleteByTherapistIdAndDate(therapistId, date)
}
