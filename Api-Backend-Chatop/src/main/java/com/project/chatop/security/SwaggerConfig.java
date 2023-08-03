package com.project.chatop.security;

import java.util.List;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
public class SwaggerConfig {

    @Bean
    OpenAPI myOpenAPI() {
        Server devServer = new Server();
        devServer.setUrl("http://localhost:3001"); // Remplacez par l'URL de votre serveur backend
        devServer.setDescription("Serveur utilisé");

        Contact contact = new Contact();
        contact.setEmail("sabri.derbala@gmail.com");
        contact.setName("Sabri");

        Info info = new Info()
                .title("Tutorial Management API")
                .version("1.0")
                .contact(contact)
                .description("This API exposes endpoints of ChatOpProject.");

        return new OpenAPI().info(info)
                .servers(List.of(devServer))
                .addSecurityItem(new SecurityRequirement().addList("JWT", "read"))
                .components(new Components().addSecuritySchemes("JWT",
                        new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT")));
    }

}