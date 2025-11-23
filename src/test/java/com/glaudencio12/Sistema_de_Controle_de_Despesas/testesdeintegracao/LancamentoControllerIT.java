package com.glaudencio12.Sistema_de_Controle_de_Despesas.testesdeintegracao;

import com.glaudencio12.Sistema_de_Controle_de_Despesas.testesdeintegracao.testcontainer.TestContainerMySQL;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LancamentoControllerIT extends TestContainerMySQL {

    @LocalServerPort
    private int port;

    @BeforeEach
    void setup(){
        RestAssured.port = this.port;
        RestAssured.baseURI = "http://localhost";
    }
}
