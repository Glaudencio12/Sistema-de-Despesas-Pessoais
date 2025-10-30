package com.glaudencio12.Sistema_de_Controle_de_Despesas.controllers.docs;

import com.glaudencio12.Sistema_de_Controle_de_Despesas.dto.loginUser.LoginRequestDTO;
import com.glaudencio12.Sistema_de_Controle_de_Despesas.dto.loginUser.TokenDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

public interface AuthControllerDocs {
    @Operation(summary = "Autentica o usuário - USER e ADMIN", description = "Realiza a autenticação do usuário no sistema via email e senha, retornando o token de acesso",
            tags = {"Login"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "Usuário autenticado com sucesso",
                            content = @Content(schema = @Schema(implementation = TokenDTO.class))
                    ),
                    @ApiResponse(responseCode = "400", description = "Requisição inválida (Dados ausentes ou mal formados)", content = @Content),
                    @ApiResponse(responseCode = "401", description = "Credenciais inválidas ou não autorizado", content = @Content),
                    @ApiResponse(responseCode = "404", description = "Usuário não encontrado", content = @Content),
                    @ApiResponse(responseCode = "500", description = "Erro interno no servidor", content = @Content)
            }
    )
    ResponseEntity<?> login(@Valid @RequestBody LoginRequestDTO credenciais);

    @Operation(summary = "Gera novos tokens - USER e ADMIN", description = "Gera um novo token de acesso e atualização a partir de um refresh token válido",
            tags = {"Login"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "Tokens gerados com sucesso",
                            content = @Content(schema = @Schema(implementation = TokenDTO.class))
                    ),
                    @ApiResponse(responseCode = "400", description = "Requisição inválida", content = @Content),
                    @ApiResponse(responseCode = "401", description = "Refresh Token inválido, expirado ou malformado.", content = @Content),
                    @ApiResponse(responseCode = "403", description = "Acesso negado (Token válido, mas sem permissão)", content = @Content),
                    @ApiResponse(responseCode = "404", description = "Usuário não encontrado", content = @Content),
                    @ApiResponse(responseCode = "500", description = "Erro interno no servidor", content = @Content)
            }
    )
    public ResponseEntity<TokenDTO> refreshToken(@PathVariable("email") String email, @RequestHeader("Authorization") String refreshToken);
}
