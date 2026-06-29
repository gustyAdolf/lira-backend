package com.lira.infrastructure.report

import com.lira.domain.report.Report
import org.springframework.stereotype.Component
import org.thymeleaf.TemplateEngine
import org.thymeleaf.context.Context
import org.xhtmlrenderer.pdf.ITextRenderer
import java.io.ByteArrayOutputStream

@Component
class ThymeleafPdfAdapter(private val templateEngine: TemplateEngine) : ReportPdfGenerator {

    override fun generate(report: Report): ByteArray {
        val context = Context().apply {
            // Pasamos el objeto report completo para tener: report.patient.name, report.createdAt, etc.
            setVariable("report", report)
            // Pasamos el mapa data por separado para mantener la compatibilidad con lo que ya tengas
            setVariables(report.data)
        }

        val templateName = when(report.type.id) {
            1 -> "reports/derivation" // Es mejor organizar por carpetas
            else -> "reports/generico"
        }

        val htmlContent = templateEngine.process(templateName, context)

        val outputStream = ByteArrayOutputStream()
        val renderer = ITextRenderer()

        // TIP: Si usas caracteres especiales (tildes, Ñ), Flying Saucer a veces sufre.
        // Es mejor asegurar el renderizado:
        renderer.setDocumentFromString(htmlContent)
        renderer.layout()
        renderer.createPDF(outputStream)

        return outputStream.toByteArray()
    }
}