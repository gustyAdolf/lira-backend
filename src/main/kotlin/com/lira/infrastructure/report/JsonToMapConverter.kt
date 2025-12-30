package com.lira.infrastructure.report

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter

@Converter
class JsonToMapConverter : AttributeConverter<Map<String, Any>, String> {

    // Usamos ObjectMapper de Jackson para la serialización/deserialización
    private val objectMapper: ObjectMapper = jacksonObjectMapper()

    // --- CONVERSIÓN A BASE DE DATOS (Map -> String JSON) ---
    override fun convertToDatabaseColumn(attribute: Map<String, Any>?): String? {
        return try {
            if (attribute == null) return null
            // Convierte el mapa de Kotlin en una cadena JSON
            objectMapper.writeValueAsString(attribute)
        } catch (e: Exception) {
            // Manejo de errores
            throw RuntimeException("Error al serializar Map a JSON.", e)
        }
    }

    // --- CONVERSIÓN DESDE BASE DE DATOS (String JSON -> Map) ---
    override fun convertToEntityAttribute(dbData: String?): Map<String, Any>? {
        return try {
            if (dbData == null) return emptyMap()
            // Convierte la cadena JSON de la BBDD en un mapa de Kotlin
            objectMapper.readValue<Map<String, Any>>(dbData)
        } catch (e: Exception) {
            // Manejo de errores
            throw RuntimeException("Error al deserializar JSON a Map.", e)
        }
    }
}