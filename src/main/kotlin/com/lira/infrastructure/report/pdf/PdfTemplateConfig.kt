package com.lira.infrastructure.report.pdf

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.thymeleaf.spring6.SpringTemplateEngine
import org.thymeleaf.templatemode.TemplateMode
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver

@Configuration
class PdfTemplateConfig {

    @Bean("pdfTemplateEngine")
    fun pdfTemplateEngine(): SpringTemplateEngine {
        val resolver = ClassLoaderTemplateResolver().apply {
            prefix = "/templates/reports/"
            suffix = ".html"
            templateMode = TemplateMode.XML
            characterEncoding = "UTF-8"
            isCacheable = false
        }
        return SpringTemplateEngine().apply {
            addTemplateResolver(resolver)
        }
    }
}
