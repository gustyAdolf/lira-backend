package com.lira.application.progressplan

import com.lira.domain.progressplan.ProgressPlanRepository
import com.lira.domain.progressplan.SubobjectiveEntry
import org.springframework.stereotype.Service

@Service
class GetStandaloneEntries(private val repository: ProgressPlanRepository) {
    fun execute(planId: Int): List<SubobjectiveEntry> =
        repository.getStandaloneEntriesByPlanId(planId)
}
