package com.spotfinderbackend.shared.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
public class CorsConfig {

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        // Leer la variable de entorno CORS_ORIGINS, o usar fallback
        String originsEnv = System.getenv("CORS_ORIGINS");
        List<String> allowedOrigins = (originsEnv != null && !originsEnv.isBlank())
                ? Arrays.asList(originsEnv.split(","))
                : List.of("https://spot-finder-frontend.vercel.app", "http://localhost:4200");

        config.setAllowedOrigins(allowedOrigins);
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        
        // Evitamos usar "*" en los headers cuando allowCredentials es true, es más seguro y evita errores estrictos del navegador
        config.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "Accept"));
        
        config.setAllowCredentials(true);
        config.setExposedHeaders(Arrays.asList("Authorization", "Content-Disposition"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        
        return source; // <-- Retornamos el source directamente, no un CorsFilter
    }
}
