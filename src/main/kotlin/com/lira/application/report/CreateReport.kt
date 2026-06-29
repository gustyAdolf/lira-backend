package com.lira.application.report

import com.lira.domain.appointment.Appointment
import com.lira.domain.appointment.AppointmentRepository
import com.lira.domain.report.Report
import com.lira.domain.report.ReportRepository
import com.lira.domain.report.ReportTypeEnum
import com.lira.infrastructure.appointment.dto.AppointmentRequest
import com.lira.infrastructure.appointment.dto.toDomain
import com.lira.infrastructure.report.dto.ReportRequest
import com.lira.infrastructure.report.dto.toDomain
import org.springframework.stereotype.Service

@Service
class CreateReport(
    private val reportRepository: ReportRepository
) {
    fun execute(request: ReportRequest): Report {
        val reportType = ReportTypeEnum.fromId(request.reportTypeId)
            ?: throw IllegalArgumentException("Tipo de informe ID ${request.reportTypeId} no válido.")


        // 3. Guardar usando el Puerto
        return reportRepository.save(request.toDomain())
    }
}