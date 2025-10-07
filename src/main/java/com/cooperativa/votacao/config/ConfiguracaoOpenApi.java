package com.cooperativa.votacao.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConfiguracaoOpenApi {
    @Bean
    public OpenAPI api() {
        return new OpenAPI().info(
            new Info()
                .title("API - Sistema de Votação")
                .version("1.0.0")
                .description("API REST para votos")
        );
    }
}