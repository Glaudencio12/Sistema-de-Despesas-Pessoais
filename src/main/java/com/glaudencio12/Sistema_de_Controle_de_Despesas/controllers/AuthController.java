package com.glaudencio12.Sistema_de_Controle_de_Despesas.controllers;

import com.glaudencio12.Sistema_de_Controle_de_Despesas.dto.loginUser.LoginRequestDTO;
import com.glaudencio12.Sistema_de_Controle_de_Despesas.services.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("auth/")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
    Recebe as credenciais do usuário para a realização da autenticação autenticação
     */
    @PostMapping("/signin")
    public ResponseEntity<?> signin(@Valid @RequestBody LoginRequestDTO credenciais) {
        return authService.signIn(credenciais);
    }
}
