package com.taskmaster.taskmaster.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {

  @Override
  public void addCorsMappings(CorsRegistry registry) {
    registry.addMapping("/**")
            .allowedOrigins("*") // Permite qualquer origem
            .allowedMethods("*") // Permite todos os métodos HTTP
            .allowedHeaders("*") // Permite todos os cabeçalhos
            .allowCredentials(false); // Desativa envio de cookies/autenticação
  }
}

