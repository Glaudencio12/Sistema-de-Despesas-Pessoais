package com.glaudencio12.Sistema_de_Controle_de_Despesas.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "API de Gerenciamento Financeiro Pessoal",
                version = "1.0.0",
                description = "Sistema de Controle de Despesas é uma aplicação Spring Boot desenvolvida " +
                              "para gerenciar despesas e receitas pessoais. O sistema permite aos usuários " +
                              "cadastrar categorias de gastos, registrar lançamentos financeiros e acompanhar " +
                              "seu fluxo de caixa de forma organizada.",
                contact = @Contact(
                        name = "Suporte",
                        email = "devglaudencio@gmail.com"
                ),
                license = @License(
                        name = "Apache 2.0",
                        url = "https://www.apache.org/licenses/LICENSE-2.0"
                )
        ),
        servers = @Server(
                url = "http://localhost:8080",
                description = "Servidor de Desenvolvimento"
        )
)
public class OpenAPIConfig {

}
