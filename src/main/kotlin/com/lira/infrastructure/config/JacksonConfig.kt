package com.lira.infrastructure.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary

@Configuration
class JacksonConfig {

    @Bean
    @Primary
    fun objectMapper(): ObjectMapper {
        return jacksonObjectMapper()
            .registerModule(JavaTimeModule()) // Para manejar LocalDate/LocalDateTime
            .registerKotlinModule()           // Para manejar clases de Kotlin (data classes)
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS) // Para que salgan como "2026-01-18" y no como números
    }
}