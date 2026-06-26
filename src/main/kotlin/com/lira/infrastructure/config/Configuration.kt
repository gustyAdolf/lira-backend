package com.lira.infrastructure.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class Configuration : WebMvcConfigurer {

    @Value("\${user.image.path}")
    private lateinit var userImagePath: String

    @Value("\${company.image.path}")
    private lateinit var companyImagePath: String

    override fun addCorsMappings(registry: CorsRegistry) {
        registry.addMapping("/**")
            .allowedOrigins("http://localhost:4200", "http://localhost:54002", "http://localhost:8080")
            .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE")
            .allowCredentials(true)
    }

    override fun addResourceHandlers(registry: ResourceHandlerRegistry) {
        registry.addResourceHandler("/images/users/**")
            .addResourceLocations("file:$userImagePath")
        registry.addResourceHandler("/images/companies/**")
            .addResourceLocations("file:$companyImagePath")
    }
}
