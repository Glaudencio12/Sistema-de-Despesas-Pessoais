package com.glaudencio12.Sistema_de_Controle_de_Despesas.testesdeintegracao.testcontainer;

import lombok.Getter;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
public abstract class TestContainerMySQL {

    @Container
    @ServiceConnection
    static MySQLContainer<?> mySQLContainer = new MySQLContainer<>("mysql:9.1.0")
            .withCommand(
                    "--log-bin-trust-function-creators=1",
                    "--server-id=1"
            );
    ;
}
