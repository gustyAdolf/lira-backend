package com.lira.application.therapistschedule

import com.lira.domain.therapistschedule.TherapistScheduleException
import com.lira.domain.therapistschedule.TherapistScheduleExceptionRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class GetScheduleExceptions(
    private val repository: TherapistScheduleExceptionRepository,
) {
    fun execute(therapistId: Int): List<TherapistScheduleException> =
        repository.findByTherapistId(therapistId)
}
