package com.glaudencio12.Sistema_de_Controle_de_Despesas.controllers;

import com.glaudencio12.Sistema_de_Controle_de_Despesas.controllers.docs.AuthControllerDocs;
import com.glaudencio12.Sistema_de_Controle_de_Despesas.dto.loginUser.LoginRequestDTO;
import com.glaudencio12.Sistema_de_Controle_de_Despesas.dto.loginUser.TokenDTO;
import com.glaudencio12.Sistema_de_Controle_de_Despesas.services.AuthService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/auth/")
@Tag(name = "Login", description = "Esta seção possui o endpoint responsável pela autenticação do usuário.")
public class AuthController implements AuthControllerDocs {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
    Recebe as credenciais do usuário para a realização da autenticação autenticação
     */
    @PostMapping(
            value = "/login",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE},
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE})
    @Override
    public ResponseEntity<TokenDTO> login(@Valid @RequestBody LoginRequestDTO credenciais) {
        TokenDTO token = authService.login(credenciais);
        return ResponseEntity.ok(token);
    }

    @PutMapping(
            value = "/refresh/{username}",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE},
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE})
    public ResponseEntity<TokenDTO> refreshToken(@PathVariable("email") String email, @RequestHeader("Authorization") String refreshToken) {
        TokenDTO token = authService.refreshToken(email, refreshToken);
        return ResponseEntity.ok(token);
    }
}
