package com.phobos.infrastructure.config

import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class Configuration : WebMvcConfigurer {
    override fun addCorsMappings(registry: CorsRegistry) {
        registry.addMapping("/**")
            .allowedOrigins("http://localhost:4200") // Permitir solo desde el dominio del frontend
            .allowedMethods("GET", "POST", "PUT", "DELETE") // Métodos permitidos
            .allowCredentials(true); // Permitir credenciales (cookies, tokens, etc.)
    }
}