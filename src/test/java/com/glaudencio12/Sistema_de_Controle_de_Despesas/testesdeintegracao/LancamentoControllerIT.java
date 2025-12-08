package com.glaudencio12.Sistema_de_Controle_de_Despesas.testesdeintegracao;

import com.glaudencio12.Sistema_de_Controle_de_Despesas.dto.loginUser.LoginRequestDTO;
import com.glaudencio12.Sistema_de_Controle_de_Despesas.dto.request.LancamentoRequestDTO;
import com.glaudencio12.Sistema_de_Controle_de_Despesas.models.Categoria;
import com.glaudencio12.Sistema_de_Controle_de_Despesas.models.Usuario;
import com.glaudencio12.Sistema_de_Controle_de_Despesas.models.enums.TipoLancamentoCategoria;
import com.glaudencio12.Sistema_de_Controle_de_Despesas.repository.CategoriaRepository;
import com.glaudencio12.Sistema_de_Controle_de_Despesas.repository.LancamentoRepository;
import com.glaudencio12.Sistema_de_Controle_de_Despesas.repository.UsuarioRepository;
import com.glaudencio12.Sistema_de_Controle_de_Despesas.testesdeintegracao.testcontainer.TestContainerMySQL;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.hamcrest.Matchers.*;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class LancamentoControllerIT extends TestContainerMySQL {

    @LocalServerPort
    private int port;

    @Autowired
    UsuarioRepository usuarioRepository;

    @Autowired
    CategoriaRepository categoriaRepository;

    @Autowired
    LancamentoRepository lancamentoRepository;

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    private String tokenDeAcesso;
    private Long usuarioId;
    private Categoria categoria;
    private LancamentoRequestDTO lancamentoRequest;

    @BeforeEach
    void setup(){
        RestAssured.port = this.port;
        RestAssured.baseURI = "http://localhost";

        usuarioRepository.deleteAll();
        categoriaRepository.deleteAll();

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

        categoria = new Categoria();
        categoria.setNome("Casa");
        categoria.setTipo(TipoLancamentoCategoria.DESPESA);
        categoria.setUsuario(user);
        categoriaRepository.save(categoria);

        lancamentoRequest = new LancamentoRequestDTO();
        lancamentoRequest.setDescricao("Lancamento de teste");
        lancamentoRequest.setValor(BigDecimal.valueOf(2.50));
        lancamentoRequest.setTipo(categoria.getTipo());
        lancamentoRequest.setCategoria(categoria.getNome());
        lancamentoRequest.setUsuarioId(usuarioId);
    }

    @Test
    @Order(1)
    void deve_registrar_um_lancamento(){

        RestAssured.given()
                    .contentType(ContentType.JSON)
                    .header("Authorization", "Bearer " + tokenDeAcesso)
                    .body(lancamentoRequest)
                .when()
                    .post("/api/lancamentos")
                .then()
                    .statusCode(201)
                    .body("descricao", equalTo(lancamentoRequest.getDescricao()))
                    .body("valor", equalTo(lancamentoRequest.getValor().floatValue()))
                    .body("categoria", equalTo(lancamentoRequest.getCategoria()))
                    .body("tipo", equalTo(lancamentoRequest.getTipo().name()))
                .log()
                    .body();
    }

    @Test
    @Order(2)
    void deve_tentar_registrar_um_lancamento_com_id_do_usuario_incorreto(){

        lancamentoRequest.setUsuarioId(usuarioId + 2);

        RestAssured.given()
                    .contentType(ContentType.JSON)
                    .header("Authorization", "Bearer " + tokenDeAcesso)
                    .body(lancamentoRequest)
                .when()
                    .post("/api/lancamentos")
                .then()
                    .statusCode(404)
                    .body("mensagem", equalTo("Usuário não encontrado"))
                .log()
                    .body();
    }

    @Test
    @Order(3)
    void deve_lancar_uma_excecao_se_nao_encontrar_a_categoria_especificada(){
        lancamentoRequest.setCategoria("Carro");

        RestAssured.given()
                    .contentType(ContentType.JSON)
                    .header("Authorization", "Bearer " + tokenDeAcesso)
                    .body(lancamentoRequest)
                .when()
                    .post("/api/lancamentos")
                .then()
                    .statusCode(404)
                    .body("mensagem", equalTo("Categoria não encontrada"))
                .log()
                    .body();
    }

    @Test
    @Order(4)
    void deve_buscar_um_lancamento_por_id(){

        Integer idInt = RestAssured.given()
                    .contentType(ContentType.JSON)
                    .header("Authorization", "Bearer " + tokenDeAcesso)
                    .body(lancamentoRequest)
                .when()
                    .post("/api/lancamentos")
                .then()
                    .statusCode(201)
                .extract()
                    .path("id");

        RestAssured.given()
                    .contentType(ContentType.JSON)
                    .header("Authorization", "Bearer " + tokenDeAcesso)
                .when()
                    .get("/api/lancamentos/{id}", idInt.longValue())
                .then()
                    .statusCode(200)
                    .body("descricao", equalTo(lancamentoRequest.getDescricao()))
                    .body("valor", equalTo(lancamentoRequest.getValor().floatValue()))
                    .body("categoria", equalTo(lancamentoRequest.getCategoria()))
                    .body("tipo", equalTo(lancamentoRequest.getTipo().name()))
                .log()
                    .body();
    }

    @Test
    @Order(4)
    void deve_lancar_uma_excecao_se_o_id_do_lancamento_for_incorreto(){

        Integer idInt = RestAssured.given()
                    .contentType(ContentType.JSON)
                    .header("Authorization", "Bearer " + tokenDeAcesso)
                    .body(lancamentoRequest)
                .when()
                    .post("/api/lancamentos")
                .then()
                    .statusCode(201)
                .extract()
                    .path("id");

        RestAssured.given()
                    .contentType(ContentType.JSON)
                    .header("Authorization", "Bearer " + tokenDeAcesso)
                .when()
                    .get("/api/lancamentos/{id}", idInt.longValue() + 1)
                .then()
                    .statusCode(404)
                    .body("mensagem", equalTo("Lançamento não encontrado"))
                .log()
                    .body();
    }

    @Test
    @Order(5)
    void deve_listar_lancamentos_do_usuario_autenticado() {

        RestAssured.given()
                    .contentType(ContentType.JSON)
                    .header("Authorization", "Bearer " + tokenDeAcesso)
                    .body(lancamentoRequest)
                .when()
                    .post("/api/lancamentos")
                .then()
                    .statusCode(201);

        RestAssured.given()
                    .contentType(ContentType.JSON)
                    .header("Authorization", "Bearer " + tokenDeAcesso)
                .when()
                    .get("/api/lancamentos")
                .then()
                    .statusCode(200)
                    .body("_embedded.Lancamento.size()", greaterThan(0))
                    .body("_links.self.href", notNullValue())
                    .body("page.totalElements", greaterThan(0))
                .log()
                    .body();
    }

    @Test
    @Order(6)
    void deve_retornar_excecao_quando_usuario_autenticado_nao_possuir_lancamentos() {
        lancamentoRepository.deleteAll();

        RestAssured.given()
                    .contentType(ContentType.JSON)
                    .header("Authorization", "Bearer " + tokenDeAcesso)
                .when()
                    .get("/api/lancamentos")
                .then()
                    .statusCode(404)
                    .body("mensagem", equalTo("Nenhum lançamento encontrada para este usuário"))
                .log()
                .body();
    }
}
