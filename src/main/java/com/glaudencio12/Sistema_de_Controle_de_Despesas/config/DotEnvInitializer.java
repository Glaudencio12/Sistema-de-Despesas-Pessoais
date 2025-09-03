package com.glaudencio12.Sistema_de_Controle_de_Despesas.config;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;

public class DotEnvInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        Dotenv dotenv = Dotenv.configure().ignoreIfMissing()
                .directory(System.getenv("DOTENV_PATH") != null ? System.getenv("DOTENV_PATH") : ".").load();

        System.setProperty("DB_URL_LOCAL", dotenv.get("DB_URL_LOCAL"));
        System.setProperty("DB_USER_LOCAL", dotenv.get("DB_USER_LOCAL"));
        System.setProperty("DB_PASSWORD_LOCAL", dotenv.get("DB_PASSWORD_LOCAL"));
    }
}
