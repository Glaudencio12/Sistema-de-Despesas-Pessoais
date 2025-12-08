package com.glaudencio12.Sistema_de_Controle_de_Despesas.testesdeintegracao;

import com.auth0.jwt.JWT;
import com.glaudencio12.Sistema_de_Controle_de_Despesas.dto.loginUser.LoginRequestDTO;
import com.glaudencio12.Sistema_de_Controle_de_Despesas.dto.request.CategoriaRequestDTO;
import com.glaudencio12.Sistema_de_Controle_de_Despesas.models.Categoria;
import com.glaudencio12.Sistema_de_Controle_de_Despesas.models.Usuario;
import com.glaudencio12.Sistema_de_Controle_de_Despesas.models.enums.TipoLancamentoCategoria;
import com.glaudencio12.Sistema_de_Controle_de_Despesas.repository.CategoriaRepository;
import com.glaudencio12.Sistema_de_Controle_de_Despesas.repository.UsuarioRepository;
import com.glaudencio12.Sistema_de_Controle_de_Despesas.testesdeintegracao.testcontainer.TestContainerMySQL;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDate;
import java.util.Date;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.greaterThan;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CategoriaControllerIT extends TestContainerMySQL {

    @LocalServerPort
    private int port;

    @Autowired
    UsuarioRepository usuarioRepository;

    @Autowired
    CategoriaRepository categoriaRepository;

    @Autowired
    ModelMapper mapper = new ModelMapper();

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    private String tokenDeAcesso;
    private Long usuarioId;
    private Long idCategoria;
    private CategoriaRequestDTO categoria;

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


        categoria = new CategoriaRequestDTO();
        categoria.setNome("Casa");
        categoria.setTipo(TipoLancamentoCategoria.DESPESA);
        categoria.setUsuarioId(usuarioId);
        idCategoria = categoriaRepository.save(mapper.map(categoria, Categoria.class)).getId();
    }

    @Test
    @Order(1)
    void deve_criar_uma_categoria(){
        CategoriaRequestDTO categoria = new CategoriaRequestDTO();
        categoria.setNome("Carro");
        categoria.setTipo(TipoLancamentoCategoria.DESPESA);
        categoria.setUsuarioId(usuarioId);

        RestAssured.given()
                    .contentType(ContentType.JSON)
                    .header("Authorization", "Bearer " + tokenDeAcesso)
                    .body(categoria)
                .when()
                    .post("/api/categorias")
                .then()
                    .statusCode(201)
                    .body("nome", equalTo(categoria.getNome()))
                    .body("tipo", equalTo(categoria.getTipo().name()))
                .log()
                    .body();
    }

    @Test
    @Order(2)
    void tenta_criar_uma_categoria_com_mesmo_nome_de_uma_ja_cadastrada(){
        CategoriaRequestDTO categoria = new CategoriaRequestDTO();
        categoria.setNome("Casa");
        categoria.setTipo(TipoLancamentoCategoria.DESPESA);
        categoria.setUsuarioId(usuarioId);

        RestAssured.given()
                .contentType(ContentType.JSON)
                    .header("Authorization", "Bearer " + tokenDeAcesso)
                    .body(categoria)
                .when()
                    .post("/api/categorias")
                .then()
                    .statusCode(409)
                    .body("mensagem", equalTo("A categoria fornecida já está cadastrada na base de dados"))
                .log()
                    .body();
    }

    @Test
    @Order(3)
    void tenta_criar_uma_categoria_com_id_de_usuario_inexistente(){
        CategoriaRequestDTO categoria = new CategoriaRequestDTO();
        categoria.setNome("Casa");
        categoria.setTipo(TipoLancamentoCategoria.DESPESA);
        categoria.setUsuarioId(usuarioId + 2); // MODIFICA O ID

        RestAssured.given()
                    .contentType(ContentType.JSON)
                    .header("Authorization", "Bearer " + tokenDeAcesso)
                .body(categoria)
                    .when()
                .post("/api/categorias")
                    .then()
                    .statusCode(404)
                    .body("mensagem", equalTo("O usuário informado não foi encontrado na base de dados"))
                .log()
                    .body();
    }

    @Test
    @Order(4)
    void deve_buscar_uma_categoria_por_id(){
        RestAssured.given()
                    .contentType(ContentType.JSON)
                    .header("Authorization", "Bearer " + tokenDeAcesso)
                .when()
                    .get("/api/categorias/{id}", idCategoria)
                .then()
                    .statusCode(200)
                    .body("nome", equalTo(categoria.getNome()))
                    .body("tipo", equalTo(categoria.getTipo().name()))
                .log()
                    .body();
    }

    @Test
    @Order(5)
    void deve_buscar_uma_categoria_por_id_inexistente(){
        RestAssured.given()
                    .contentType(ContentType.JSON)
                    .header("Authorization", "Bearer " + tokenDeAcesso)
                .when()
                    .get("/api/categorias/{id}", idCategoria + 1)
                .then()
                    .statusCode(404)
                    .body("mensagem", equalTo("Categoria não encontrada"))
                .log()
                    .body();
    }

    @Test
    @Order(6)
    void deve_retornar_todos_as_categorias(){
        RestAssured.given()
                    .contentType(ContentType.JSON)
                    .header("Authorization", "Bearer " + tokenDeAcesso)
                .when()
                    .get("/api/categorias")
                .then()
                    .statusCode(200)
                    .body("_embedded.Categoria.size()", greaterThan(0))
                    .body("_links.self.href", notNullValue())
                    .body("page.totalElements", greaterThan(0))
                .log()
                    .body();
    }

    @Test
    @Order(7)
    void deve_retornar_excecao_quando_usuario_nao_possuir_categorias() {
        categoriaRepository.deleteAll();

        RestAssured.given()
                    .contentType(ContentType.JSON)
                    .header("Authorization", "Bearer " + tokenDeAcesso)
                .when()
                    .get("/api/categorias")
                .then()
                    .statusCode(404)
                    .body("mensagem", equalTo("Nenhuma categoria encontrada para este usuário"))
                .log()
                    .body();
    }

    @Test
    @Order(8)
    void deve_excluir_uma_categoria_pelo_nome(){
        RestAssured.given()
                    .contentType(ContentType.JSON)
                    .header("Authorization", "Bearer " + tokenDeAcesso)
                .when()
                    .delete("/api/categorias/{nomeCategoria}", categoria.getNome())
                .then()
                    .statusCode(204)
                .log()
                    .body();
    }

    @Test
    @Order(8)
    void deve_lancar_uma_excecao_se_o_nome_da_categoria_nao_for_encontrado(){
        RestAssured.given()
                    .contentType(ContentType.JSON)
                    .header("Authorization", "Bearer " + tokenDeAcesso)
                .when()
                    .delete("/api/categorias/{nomeCategoria}", "CategoriaNomeIncorreto")
                .then()
                    .statusCode(404)
                    .body("mensagem", equalTo("A categoria fornecida não foi cadastrada"))
                .log()
                    .body();
    }
}
