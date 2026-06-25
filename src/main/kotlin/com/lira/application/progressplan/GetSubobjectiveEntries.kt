package com.lira.application.progressplan

import com.lira.domain.progressplan.ProgressPlanRepository
import com.lira.domain.progressplan.SubobjectiveEntry
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class GetSubobjectiveEntries(
    private val progressPlanRepository: ProgressPlanRepository
) {
    fun execute(subobjectiveId: Int, limit: Int = 3): List<SubobjectiveEntry> {
        return progressPlanRepository.getRecentSubobjectiveEntries(subobjectiveId, limit)
    }
}
