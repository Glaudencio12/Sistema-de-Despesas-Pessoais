package com.glaudencio12.Sistema_de_Controle_de_Despesas.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfig {

    @Bean
    OpenAPI openAPI() {
        return new OpenAPI().info(new Info()
                .title("API de Gerenciamento Financeiro Pessoal")
                .version("1.0.0")
                .description("""
                    A API de Gerenciamento Financeiro Pessoal é um serviço RESTful desenvolvido em Java com Spring Boot, projetado para auxiliar usuários no controle completo de suas finanças pessoais.
        
                    Funcionalidades principais:
        
                    - Cadastro e gerenciamento de usuários.
                    - Criação e organização de categorias financeiras (receitas e despesas).
                    - Registro detalhado de lançamentos financeiros com validações por tipo e categoria.
                    - Respostas enriquecidas com links REST (HATEOAS), facilitando a navegação entre recursos.
        
                    Essa API busca oferecer uma experiência intuitiva, segura e confiável para o controle financeiro pessoal.
                """)
                .contact(new Contact()
                        .name("Glaudencio da Costa Meneses")
                        .email("devglaudencio@gmail.com")
                )
        );
    }

}
