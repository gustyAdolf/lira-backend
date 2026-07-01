package com.lira.infrastructure.report.pdf

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.lira.infrastructure.report.ReportEntity
import com.lira.infrastructure.user.entity.PatientEntity
import com.lira.infrastructure.user.entity.TherapistEntity
import com.lira.infrastructure.user.jpa.JpaCompanyRepository
import com.lira.infrastructure.user.jpa.JpaUserRepository
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service
import org.thymeleaf.spring6.SpringTemplateEngine
import org.thymeleaf.context.Context
import org.xhtmlrenderer.pdf.ITextRenderer
import java.io.ByteArrayOutputStream
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@Service
class ReportPdfService(
    @Qualifier("pdfTemplateEngine") private val templateEngine: SpringTemplateEngine,
    private val jpaUserRepository: JpaUserRepository,
    private val jpaCompanyRepository: JpaCompanyRepository,
    private val objectMapper: ObjectMapper
) {

    private val spanishDateFormatter = DateTimeFormatter.ofPattern("d 'de' MMMM 'de' yyyy", Locale("es", "ES"))

    fun generate(report: ReportEntity): ByteArray {
        val therapist = jpaUserRepository.findById(report.therapistId).orElseThrow {
            NoSuchElementException("Therapist ${report.therapistId} not found")
        }
        val patient = jpaUserRepository.findById(report.userId).orElseThrow {
            NoSuchElementException("Patient ${report.userId} not found")
        }

        val licenseNumber = (therapist as? TherapistEntity)?.licenseNumber ?: "—"
        val patientEntity = patient as? PatientEntity
        val patientBirthdate = patientEntity?.birthdate
            ?.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) ?: "—"
        val patientDni = patientEntity?.dni ?: "—"

        val companyId = jpaCompanyRepository.findCompanyIdByTherapistId(report.therapistId)
        val company = companyId?.let { jpaCompanyRepository.findById(it).orElse(null) }

        val ctx = Context(Locale("es", "ES")).apply {
            setVariable("centro", mapOf(
                "nombre" to (company?.name ?: "—"),
                "nif" to (company?.cif ?: "—"),
                "direccion" to (company?.companyAddress ?: company?.address ?: "—"),
                "telefono" to (company?.telephone ?: "—"),
                "email" to (company?.email ?: "—")
            ))
            setVariable("profesional", mapOf(
                "nombre" to therapist.name,
                "colegiado" to licenseNumber
            ))
            setVariable("paciente", mapOf(
                "nombre" to patient.name,
                "documento" to patientDni,
                "fechaNacimiento" to patientBirthdate,
                "contacto" to (patient.telephone ?: "—")
            ))
            setVariable("fechaEmision", LocalDate.now().format(spanishDateFormatter))
            setVariable("datos", objectMapper.readValue<Map<String, Any>>(report.dataReport))
        }

        val templateName = when (report.reportTypeId) {
            1 -> "derivacion"
            2 -> "justificante"
            3 -> "comunicacion_clinica"
            4 -> "certificado_tratamiento"
            else -> throw IllegalArgumentException("Unknown report type: ${report.reportTypeId}")
        }

        val html = templateEngine.process(templateName, ctx)
        return ByteArrayOutputStream().use { bos ->
            ITextRenderer().also { renderer ->
                renderer.setDocumentFromString(html, "")
                renderer.layout()
                renderer.createPDF(bos)
            }
            bos.toByteArray()
        }
    }
}
