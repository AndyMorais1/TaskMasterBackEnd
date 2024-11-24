package com.taskmaster.taskmaster.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringDocOpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(
                        new Info()
                                .title("Task Master API")
                                .description("API para um gestor de tarefas")
                        .version("1.0")
                                .contact(
                                        new Contact().name("Jose Morais").email("moraisandy76@gmail.com")
                                )
                                .contact(
                                        new Contact().name("Edson Manuel").email("edsonfernandesfernandes01@gmail.com")
                                )
                );
    }

}
