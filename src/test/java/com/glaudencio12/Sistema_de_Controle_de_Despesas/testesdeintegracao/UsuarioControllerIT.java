package com.glaudencio12.Sistema_de_Controle_de_Despesas.testesdeintegracao;

import com.glaudencio12.Sistema_de_Controle_de_Despesas.dto.loginUser.LoginRequestDTO;
import com.glaudencio12.Sistema_de_Controle_de_Despesas.dto.request.UsuarioRequestDTO;
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
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UsuarioControllerIT extends TestContainerMySQL{

    @LocalServerPort
    private int port;

    @Autowired
    UsuarioRepository usuarioRepository;

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    private String tokenDeAcesso;
    private Long usuarioId;

    @BeforeEach
    void setup(){
        RestAssured.port = this.port;
        RestAssured.baseURI = "http://localhost";

        usuarioRepository.deleteAll();

        Usuario user = new Usuario();
        user.setEmail("teste@email.com");
        user.setDataCadastro(LocalDate.now());
        user.setNome("NomeTeste");
        user.setPapel("ADMIN");
        user.setSenha(encoder.encode("12345678"));
        usuarioId = usuarioRepository.save(user).getId();

        tokenDeAcesso = RestAssured.given()
                    .contentType(ContentType.JSON)
                    .body(new LoginRequestDTO("teste@email.com", "12345678"))
                .when()
                    .post("/api/auth/login")
                .then()
                    .statusCode(200)
                .extract()
                    .path("tokenDeAcesso");
    }

    @Test
    @Order(1)
    void cadastra_um_usuario_no_sistema(){
        UsuarioRequestDTO newUser = new UsuarioRequestDTO();
        newUser.setEmail("teste2@email.com");
        newUser.setNome("NomeTeste");
        newUser.setPapel("USER");
        newUser.setSenha("12345678");
        newUser.setDataCadastro(LocalDate.now());

        RestAssured.given()
                    .contentType(ContentType.JSON)
                    .body(newUser)
                .when()
                    .post("/api/usuarios/createUser")
                .then()
                    .statusCode(201)
                    .body("email", equalTo("teste2@email.com"))
                    .body("nome", equalTo("NomeTeste"))
                .log()
                    .body();
    }

    @Test
    @Order(2)
    void cadastra_um_usuario_com_email_ja_cadastrado(){
        UsuarioRequestDTO newUser = new UsuarioRequestDTO();
        newUser.setEmail("teste@email.com");
        newUser.setNome("NomeTeste");
        newUser.setPapel("USER");
        newUser.setSenha("12345678");
        newUser.setDataCadastro(LocalDate.now());

        RestAssured.given()
                    .contentType(ContentType.JSON)
                    .body(newUser)
                .when()
                    .post("/api/usuarios/createUser")
                .then()
                    .statusCode(409)
                    .body("mensagem", equalTo("O email fornecido já está cadastrado na base de dados"))
                .log()
                    .body();
    }

    @Test
    @Order(3)
    void busca_o_usuario_por_id(){

        RestAssured.given()
                    .contentType(ContentType.JSON)
                    .header("Authorization", "Bearer " + tokenDeAcesso)
                .when()
                    .get("/api/usuarios/{id}", usuarioId.intValue())
                .then()
                    .statusCode(200)
                    .body("id", equalTo(usuarioId.intValue()))
                    .body("email", equalTo("teste@email.com"))
                    .body("nome", equalTo("NomeTeste"))
                .log()
                    .body();
    }

    @Test
    @Order(4)
    void busca_o_usuario_por_id_incorreto(){
        RestAssured.given()
                    .contentType(ContentType.JSON)
                    .header("Authorization", "Bearer " + tokenDeAcesso)
                .when()
                    .get("/api/usuarios/{id}", 12)
                .then()
                    .statusCode(404)
                    .body("mensagem", equalTo("Usuário não encontrado"))
                .log()
                    .body();
    }

    @Test
    @Order(5)
    void busca_todos_os_usuarios(){
        RestAssured.given()
                    .contentType(ContentType.JSON)
                    .header("Authorization", "Bearer " + tokenDeAcesso)
                .when()
                    .get("/api/usuarios")
                .then()
                    .statusCode(200)
                    .body("_embedded.Usuario.size()", greaterThan(0))
                    .body("_links.self.href", notNullValue())
                    .body("page.totalElements", greaterThan(0))
                .log()
                    .body();
    }

    @Test
    @Order(6)
    void deve_atualizar_apenas_o_nome_do_usuario() {

        Map<String, Object> campos = Map.of("nome", "Novo Nome Teste");

        RestAssured.given()
                    .contentType(ContentType.JSON)
                    .header("Authorization", "Bearer " + tokenDeAcesso)
                    .body(campos)
                .when()
                    .patch("/api/usuarios/{id}", usuarioId)
                .then()
                    .statusCode(200)
                    .body("nome", equalTo("Novo Nome Teste"))
                .log()
                    .body();
    }

    @Test
    @Order(7)
    void deve_atualizar_mais_de_um_campo(){
        Map<String, Object> campos = Map.of(
                "nome", "Nome Atualizado",
                "email", "email@atualizado.com"
        );

        RestAssured.given()
                    .contentType(ContentType.JSON)
                    .header("Authorization", "Bearer " + tokenDeAcesso)
                    .body(campos)
                .when()
                    .patch("/api/usuarios/{id}", usuarioId)
                .then()
                    .statusCode(200)
                    .body("nome", equalTo("Nome Atualizado"))
                    .body("email", equalTo("email@atualizado.com"))
                .log()
                    .body();
    }

    @Test
    @Order(8)
    void deve_lancar_uma_excessao_caso_o_campo_nao_possa_ser_atualizado(){
        Map<String, Object> campo = Map.of("id", 100);

        RestAssured.given()
                    .contentType(ContentType.JSON)
                    .header("Authorization", "Bearer " + tokenDeAcesso)
                    .body(campo)
                .when()
                    .patch("/api/usuarios/{id}", usuarioId)
                .then()
                    .statusCode(400)
                .log()
                    .body();
    }

    @Test
    @Order(9)
    void deve_lancar_uma_excessao_caso_o_campo_seja_nulo(){
        Map<String, Object> campo = new HashMap<>();
        campo.put("nome", null);

        RestAssured.given()
                    .contentType(ContentType.JSON)
                    .header("Authorization", "Bearer " + tokenDeAcesso)
                    .body(campo)
                .when()
                    .patch("/api/usuarios/{id}", usuarioId)
                .then()
                    .statusCode(400)
                    .body("mensagem", equalTo("O campo 'nome' precisa ser do tipo String e não pode ser vazio"))

                .log()
                    .body();
    }

    @Test
    @Order(10)
    void deve_lancar_uma_excessao_caso_o_campo_seja_de_tipo_diferente(){
        Map<String, Object> campo = Map.of("nome", 1);

        RestAssured.given()
                    .contentType(ContentType.JSON)
                    .header("Authorization", "Bearer " + tokenDeAcesso)
                    .body(campo)
                .when()
                    .patch("/api/usuarios/{id}", usuarioId)
                .then()
                    .statusCode(400)
                    .body("mensagem", equalTo("O campo 'nome' precisa ser do tipo String e não pode ser vazio"))
                .log()
                    .body();
    }

    @Test
    @Order(11)
    void deve_deletar_usuario_por_id() {

        RestAssured.given()
                    .contentType(ContentType.JSON)
                    .header("Authorization", "Bearer " + tokenDeAcesso)
                .when()
                    .delete("/api/usuarios/{id}", usuarioId)
                .then()
                    .statusCode(204)
                .log()
                    .body();
    }

    @Test
    @Order(12)
    void deve_retornar_uma_excecao_ao_tentar_deletar_um_usuario_com_id_incorreto() {

        RestAssured.given()
                    .contentType(ContentType.JSON)
                    .header("Authorization", "Bearer " + tokenDeAcesso)
                .when()
                    .delete("/api/usuarios/{id}", usuarioId + 1)
                .then()
                    .statusCode(404)
                    .body("mensagem", equalTo("Usuário não encontrado"))
                .log()
                    .body();
    }
}
