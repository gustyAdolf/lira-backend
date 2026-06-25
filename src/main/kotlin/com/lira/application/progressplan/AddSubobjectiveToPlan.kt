package com.lira.application.progressplan

import com.lira.domain.progressplan.ProgressPlanRepository
import com.lira.domain.progressplan.Subobjective
import com.lira.infrastructure.progressplan.dto.ProgressPlanResponse
import com.lira.infrastructure.progressplan.dto.toResponse
import jakarta.transaction.Transactional
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
@Transactional
class AddSubobjectiveToPlan(private val progressPlanRepository: ProgressPlanRepository) {

    private val log = LoggerFactory.getLogger(javaClass)

    fun execute(objectiveId: Int, subobjective: Subobjective): ProgressPlanResponse {
        progressPlanRepository.saveSubobjective(subobjective, objectiveId)
        val plan = progressPlanRepository.getProgressPlanByObjectiveId(objectiveId)
        val allSubobjectives = plan.objectives.flatMap { it.subobjectives }
        val newProgress = if (allSubobjectives.isEmpty()) 0.0 else allSubobjectives.map { it.currentProgress }.average()
        progressPlanRepository.updateTotalProgress(plan.id, newProgress)
        log.info("Subobjective added to objective $objectiveId: '${subobjective.title}' [${subobjective.type}]")
        return plan.copy(totalProgress = newProgress).toResponse()
    }
}
