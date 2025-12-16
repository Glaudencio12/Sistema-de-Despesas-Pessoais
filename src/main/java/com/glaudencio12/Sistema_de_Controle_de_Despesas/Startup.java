package com.glaudencio12.Sistema_de_Controle_de_Despesas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
public class Startup {
	public static void main(String[] args) {
		SpringApplication.run(Startup.class, args);
	}
}
