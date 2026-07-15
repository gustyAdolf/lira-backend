package com.lira.infrastructure.checkin.pdf

import com.lira.application.checkin.CompanyCheckinSummary
import com.lira.infrastructure.user.jpa.JpaCompanyRepository
import com.lira.infrastructure.user.jpa.JpaUserRepository
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service
import org.thymeleaf.context.Context
import org.thymeleaf.spring6.SpringTemplateEngine
import org.xhtmlrenderer.pdf.ITextRenderer
import java.io.ByteArrayOutputStream
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@Service
class CheckinPdfService(
    @Qualifier("pdfTemplateEngine") private val templateEngine: SpringTemplateEngine,
    private val jpaUserRepository: JpaUserRepository,
    private val jpaCompanyRepository: JpaCompanyRepository,
) {
    private val spanish = Locale("es", "ES")
    private val longDateFormatter = DateTimeFormatter.ofPattern("d 'de' MMMM 'de' yyyy", spanish)
    private val shortDateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy", spanish)
    private val timeFormatter = DateTimeFormatter.ofPattern("HH:mm", spanish)

    fun generate(
        companyId: Int,
        therapistId: Int,
        from: LocalDate,
        to: LocalDate,
        summary: CompanyCheckinSummary
    ): ByteArray {
        val therapist = jpaUserRepository.findById(therapistId).orElseThrow {
            NoSuchElementException("Therapist $therapistId not found")
        }
        val company = jpaCompanyRepository.findById(companyId).orElse(null)

        val filas = summary.checkins
            .groupBy { it.checkinTime.toLocalDate() }
            .toSortedMap()
            .map { (date, sessions) ->
                mapOf(
                    "fecha" to date.format(shortDateFormatter),
                    "sesiones" to sessions.joinToString(" · ") { s ->
                        "${s.checkinTime.format(timeFormatter)}–" +
                            (s.checkoutTime?.format(timeFormatter) ?: "en curso")
                    },
                    "horas" to formatHours(sessions.sumOf { it.totalHours ?: 0.0 }),
                    "incidencia" to sessions.any { it.autoClosed }
                )
            }

        val ctx = Context(spanish).apply {
            setVariable(
                "centro", mapOf(
                    "nombre" to (company?.name ?: "—"),
                    "nif" to (company?.cif ?: "—"),
                    "direccion" to (company?.companyAddress ?: company?.address ?: "—")
                )
            )
            setVariable("terapeuta", mapOf("nombre" to therapist.name))
            setVariable(
                "periodo", mapOf(
                    "desde" to from.format(shortDateFormatter),
                    "hasta" to to.format(shortDateFormatter)
                )
            )
            setVariable(
                "resumen", mapOf(
                    "totalHoras" to formatHours(summary.totalHours),
                    "mediaDiaria" to formatHours(summary.averageDailyHours),
                    "incidencias" to summary.incidentCount
                )
            )
            setVariable("registros", filas)
            setVariable("fechaEmision", LocalDate.now().format(longDateFormatter))
        }

        val html = templateEngine.process("fichajes_empresa", ctx)
        return ByteArrayOutputStream().use { bos ->
            ITextRenderer().also { renderer ->
                renderer.setDocumentFromString(html, "")
                renderer.layout()
                renderer.createPDF(bos)
            }
            bos.toByteArray()
        }
    }

    private fun formatHours(hours: Double) = String.format(spanish, "%.2f", hours)
}
