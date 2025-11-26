package com.glaudencio12.Sistema_de_Controle_de_Despesas.testesdeintegracao;

import com.glaudencio12.Sistema_de_Controle_de_Despesas.dto.loginUser.LoginRequestDTO;
import com.glaudencio12.Sistema_de_Controle_de_Despesas.models.Usuario;
import com.glaudencio12.Sistema_de_Controle_de_Despesas.repository.UsuarioRepository;
import com.glaudencio12.Sistema_de_Controle_de_Despesas.testesdeintegracao.testcontainer.TestContainerMySQL;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDate;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AuthControllerIT extends TestContainerMySQL {

    @LocalServerPort
    private int port;

    @Autowired
    private UsuarioRepository usuarioRepository;

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @BeforeEach
    void setup() {
        RestAssured.port = this.port;
        RestAssured.baseURI = "http://localhost";

        usuarioRepository.deleteAll();

        Usuario user = new Usuario();
        user.setEmail("teste@email.com");
        user.setDataCadastro(LocalDate.now());
        user.setNome("NomeTeste");
        user.setPapel("USER");
        user.setSenha(encoder.encode("12345678"));
        usuarioRepository.save(user);
    }

    @Test
    @Order(1)
    void realiza_login_e_retorna_o_token() {

        RestAssured.given()
                    .contentType(ContentType.JSON)
                    .body(new LoginRequestDTO("teste@email.com", "12345678"))
                .when()
                    .post("api/auth/login")
                .then()
                    .statusCode(200)
                    .body("email", equalTo("teste@email.com"))
                    .body("tokenDeAcesso", notNullValue())
                    .body("tokenDeAtualizacao", notNullValue())
                .log()
                    .body();
    }

    @Test
    @Order(2)
    void tenta_realiza_login_com_credenciais_invalidas(){
        RestAssured.given()
                    .contentType(ContentType.JSON)
                    .body(new LoginRequestDTO("teste1@email.com", "12345678"))
                .when()
                    .post("api/auth/login")
                .then()
                    .statusCode(401)
                    .body("message", equalTo("Usuário inexistente ou senha inválida"))
                .log()
                    .body();
    }

    @Test
    @Order(3)
    void realiza_login_e_gera_o_refresh_token() {

        // Primeiro: login para obter o refresh token
        String refreshToken = RestAssured.given()
                            .contentType(ContentType.JSON)
                            .body(new LoginRequestDTO("teste@email.com", "12345678"))
                        .when()
                            .post("api/auth/login")
                        .then()
                            .statusCode(200)
                            .extract()
                            .path("tokenDeAtualizacao");

        // Usa o refresh token para gerar novo token
        RestAssured.given()
                    .contentType(ContentType.JSON)
                    .header("Authorization", "Bearer " + refreshToken)
                .when()
                    .put("api/auth/refresh/{email}", "teste@email.com")
                .then()
                    .statusCode(200)
                    .body("tokenDeAcesso", notNullValue())
                    .body("tokenDeAtualizacao", notNullValue())
                .log()
                    .body();
    }

    @Test
    @Order(4)
    void tenta_gerar_refresh_token_com_email_invalido(){

        String refreshToken = RestAssured.given()
                    .contentType(ContentType.JSON)
                    .body(new LoginRequestDTO("teste@email.com", "12345678"))
                .when()
                    .post("api/auth/login")
                .then()
                    .statusCode(200)
                    .extract()
                    .path("tokenDeAtualizacao");

        RestAssured.given()
                    .contentType(ContentType.JSON)
                    .header("Authorization", "Bearer " + refreshToken)
                .when()
                    .put("api/auth/refresh/{email}", "teste1@email.com")
                .then()
                    .statusCode(401)
                    .body("message", equalTo("Email teste1@email.com não encontrado"))
                .log()
                    .body();
    }
}
