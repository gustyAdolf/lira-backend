package com.lira.application.progressplan

import com.lira.domain.progressplan.PlanSessionRepository
import com.lira.infrastructure.progressplan.dto.PlanSessionRequest
import com.lira.infrastructure.progressplan.dto.PlanSessionResponse
import com.lira.infrastructure.progressplan.dto.toDomain
import com.lira.infrastructure.progressplan.dto.toResponse
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class CreatePlanSession(
    private val planSessionRepository: PlanSessionRepository
) {
    private val log = LoggerFactory.getLogger(javaClass)

    fun execute(request: PlanSessionRequest): PlanSessionResponse {
        val saved = planSessionRepository.save(request.toDomain())
        log.info("PlanSession created: id=${saved.id}, planId=${saved.planId}, appointmentId=${saved.appointmentId}")
        return saved.toResponse()
    }
}
