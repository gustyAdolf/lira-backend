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
    // Inyección del puerto (la infraestructura implementa esto)
    private val reportRepository: ReportRepository,
    // Spring inyecta todos los validadores que implementan ReportDataValidator
    allValidators: List<ReportDataValidator>
) {
    // Mapeamos los validadores por ReportType para un acceso rápido
    private val validatorMap = allValidators.associateBy { it.supports() }

    fun execute(request: ReportRequest): Report {
        val reportType = ReportTypeEnum.fromId(request.reportTypeId)
            ?: throw IllegalArgumentException("Tipo de informe ID ${request.reportTypeId} no válido.")

        // 1. Despachar la validación específica
        /*val validator = validatorMap[reportType]
            ?: throw IllegalStateException("No hay validador implementado para el tipo: ${reportType.name}")

        // Aquí se ejecuta la validación de negocio (campos obligatorios, formatos, etc.)
        validator.validate(request.reportData)*/


        // 3. Guardar usando el Puerto
        return reportRepository.save(request.toDomain())
    }
}