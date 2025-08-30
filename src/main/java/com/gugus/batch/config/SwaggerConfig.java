package com.gugus.batch.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(apiInfo())
                .servers(servers());
    }

    private Info apiInfo() {
        return new Info()
                .title("Gugus Batch API")
                .description("Gugus Batch 서비스의 REST API 문서")
                .version("3.0.1")
                .contact(new Contact()
                        .name("Gugus Team")
                        .email("dev@gugus.com")
                        .url("https://gugus.com"))
                .license(new License()
                        .name("MIT License")
                        .url("https://opensource.org/licenses/MIT"));
    }

    private List<Server> servers() {
        return List.of(
                new Server()
                        .url("http://localhost:8008")
                        .description("Local Development Server"),
                new Server()
                        .url("https://api-dev.gugus.com")
                        .description("Development Server"),
                new Server()
                        .url("https://api.gugus.com")
                        .description("Production Server")
        );
    }
}
