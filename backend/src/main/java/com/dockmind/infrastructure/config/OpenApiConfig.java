package com.dockmind.infrastructure.config;

import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;

@Configuration
@OpenAPIDefinition(info = @Info(title = "DockMind API", version = "1.0.0", description = "AI-powered developer knowledge and codebase analysis platform", contact = @Contact(name = "DockMind Team")))
public class OpenApiConfig {

}
