package com.lira.application.progressplan

import com.lira.domain.progressplan.ProgressPlanRepository
import com.lira.domain.progressplan.SubobjectiveType
import com.lira.infrastructure.progressplan.dto.ProgressPlanResponse
import com.lira.infrastructure.progressplan.dto.toResponse
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
@Transactional
class UpdateSubobjective(private val progressPlanRepository: ProgressPlanRepository) {

    fun execute(
        subobjectiveId: Int,
        objectiveId: Int,
        title: String,
        description: String?,
        targetValue: Int?,
        targetSuccess: Int?,
        targetFail: Int?
    ): ProgressPlanResponse {
        val sub = progressPlanRepository.getSubobjectiveById(subobjectiveId)

        val targetChanged = sub.targetValue != targetValue || sub.targetSuccess != targetSuccess

        val updated = sub.copy(
            title = title,
            description = description,
            targetValue = targetValue,
            targetSuccess = targetSuccess,
            targetFail = targetFail
        )

        val withProgress = if (targetChanged) {
            val newProgress = if (updated.type == SubobjectiveType.QUANTITATIVE) {
                val sum = progressPlanRepository.sumValueBySubobjective(subobjectiveId)
                (sum / (updated.targetValue?.toDouble() ?: 1.0)).coerceAtMost(1.0)
            } else {
                val successes = progressPlanRepository.countSuccessesBySubobjective(subobjectiveId)
                (successes / (updated.targetSuccess?.toDouble() ?: 1.0)).coerceAtMost(1.0)
            }
            updated.copy(currentProgress = newProgress)
        } else updated

        progressPlanRepository.updateSubobjective(withProgress, objectiveId)

        val plan = progressPlanRepository.getProgressPlanBySubobjectiveId(subobjectiveId)
        val allSubs = plan.objectives.flatMap { it.subobjectives }
        plan.totalProgress = if (allSubs.isEmpty()) 0.0 else allSubs.map { it.currentProgress }.average()
        progressPlanRepository.save(plan)

        return progressPlanRepository.getProgressPlanBySubobjectiveId(subobjectiveId).toResponse()
    }
}
