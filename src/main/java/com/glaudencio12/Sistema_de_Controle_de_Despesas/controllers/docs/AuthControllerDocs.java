package com.glaudencio12.Sistema_de_Controle_de_Despesas.controllers.docs;

import com.glaudencio12.Sistema_de_Controle_de_Despesas.dto.loginUser.LoginRequestDTO;
import com.glaudencio12.Sistema_de_Controle_de_Despesas.dto.loginUser.TokenDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public interface AuthControllerDocs {
    @Operation(summary = "Autentica o usuário", description = "Realiza a autenticação do usuário no sistema via email e senha, retornando o token de acesse",
            tags = {"Login"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "Usuário autenticado com sucesso",
                            content = @Content(schema = @Schema(implementation = TokenDTO.class))
                    ),
                    @ApiResponse(responseCode = "400", description = "Requisição inválida", content = @Content),
                    @ApiResponse(responseCode = "401", description = "Não autorizado", content = @Content),
                    @ApiResponse(responseCode = "404", description = "Usuário não encontrado", content = @Content),
                    @ApiResponse(responseCode = "500", description = "Erro interno no servidor", content = @Content)
            }
    )
    ResponseEntity<?> signin(@Valid @RequestBody LoginRequestDTO credenciais);
}
