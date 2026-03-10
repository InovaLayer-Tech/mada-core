package com.inovalayer.mada.core.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Classe de configuração de infraestrutura para permitir a comunicação entre 
 * o Front-end (Angular) e o Back-end (Spring Boot).
 * Localização: src/main/java/com/inovalayer/mada/core/config/
 */
@Configuration
public class CorsConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                // Autorizamos o Angular (porta 4200) a acessar todos os nossos endpoints
                registry.addMapping("/**")
                        .allowedOrigins("http://localhost:4200") // Origem permitida
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Verbos permitidos
                        .allowedHeaders("*") // Permite todos os cabeçalhos (essencial para autenticação futura)
                        .allowCredentials(true);
            }
        };
    }
}