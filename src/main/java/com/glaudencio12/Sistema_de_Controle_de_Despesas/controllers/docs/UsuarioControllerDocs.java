package com.glaudencio12.Sistema_de_Controle_de_Despesas.controllers.docs;

import com.glaudencio12.Sistema_de_Controle_de_Despesas.config.SecurityConfig;
import com.glaudencio12.Sistema_de_Controle_de_Despesas.dto.request.UsuarioRequestDTO;
import com.glaudencio12.Sistema_de_Controle_de_Despesas.dto.response.UsuarioResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

public interface UsuarioControllerDocs {
    @Operation(
            summary = "Cria um novo usuário - USER e ADMIN",
            description = "Cria um novo registro de usuário no sistema",
            tags = {"Usuários"},
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Usuário criado com sucesso",
                            content = @Content(schema = @Schema(implementation = UsuarioResponseDTO.class))
                    ),
                    @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content),
                    @ApiResponse(responseCode = "401", description = "Não autorizado", content = @Content),
                    @ApiResponse(responseCode = "500", description = "Erro interno", content = @Content),
                    @ApiResponse(responseCode = "409", description = "Email duplicado", content = @Content)
            }
    )
    ResponseEntity<UsuarioResponseDTO> create(@RequestBody UsuarioRequestDTO usuario);

    @Operation(
            summary = "Busca usuário por ID - ADMIN",
            description = "Retorna os dados de um usuário específico",
            tags = {"Usuários"},
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Usuário encontrado",
                            content = @Content(schema = @Schema(implementation = UsuarioResponseDTO.class))
                    ),
                    @ApiResponse(responseCode = "400", description = "Requisição inválida", content = @Content),
                    @ApiResponse(responseCode = "404", description = "Usuário não encontrado", content = @Content),
                    @ApiResponse(responseCode = "401", description = "Não autorizado (Token ausente, inválido ou expirado)", content = @Content),
                    @ApiResponse(responseCode = "403", description = "Acesso negado (Token válido, mas sem permissão)", content = @Content),
                    @ApiResponse(responseCode = "500", description = "Erro interno", content = @Content)
            },
            security = @SecurityRequirement(name = SecurityConfig.SECURITY)
    )
    ResponseEntity<UsuarioResponseDTO> findById(@Parameter(description = "Id do usuário", example = "1") @PathVariable("id") Long id);

    @Operation(
            summary = "Lista todos os usuários - ADMIN",
            description = "Retorna uma lista com todos os usuários cadastrados",
            tags = {"Usuários"},
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Lista retornada com sucesso",
                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = UsuarioResponseDTO.class)))
                    ),
                    @ApiResponse(responseCode = "204", description = "Nenhum conteúdo", content = @Content),
                    @ApiResponse(responseCode = "400", description = "Requisição inválida", content = @Content),
                    @ApiResponse(responseCode = "401", description = "Não autorizado (Token ausente, inválido ou expirado)", content = @Content),
                    @ApiResponse(responseCode = "403", description = "Acesso negado (Token válido, mas sem permissão)", content = @Content),
                    @ApiResponse(responseCode = "500", description = "Erro interno", content = @Content)
            },
            security = @SecurityRequirement(name = SecurityConfig.SECURITY)
    )
    ResponseEntity<PagedModel<EntityModel<UsuarioResponseDTO>>> findAll(
            @PageableDefault(
                    page = 0,
                    size = 12,
                    direction = Sort.Direction.ASC,
                    sort = "nome"
            ) Pageable pageable
    );

    @Operation(
            summary = "Atualiza usuário - USER e ADMIN",
            description = "Atualiza os dados de um usuário existente pelo ID",
            tags = {"Usuários"},
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Usuário atualizado com sucesso",
                            content = @Content(schema = @Schema(implementation = UsuarioResponseDTO.class))
                    ),
                    @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content),
                    @ApiResponse(responseCode = "404", description = "Usuário não encontrado", content = @Content),
                    @ApiResponse(responseCode = "401", description = "Não autorizado (Token ausente, inválido ou expirado)", content = @Content),
                    @ApiResponse(responseCode = "403", description = "Acesso negado (Token válido, mas sem permissão)", content = @Content),
                    @ApiResponse(responseCode = "500", description = "Erro interno", content = @Content),
                    @ApiResponse(responseCode = "409", description = "Email duplicado", content = @Content)
            },
            security = @SecurityRequirement(name = SecurityConfig.SECURITY)
    )
    ResponseEntity<UsuarioResponseDTO> update(@Parameter(description = "Id do usuário", example = "1") @PathVariable("id") Long id, @RequestBody UsuarioRequestDTO usuarioRequest);

    @Operation(
            summary = "Atualiza um campo específico do usuário - USER e ADMIN",
            description = "Atualiza um dado específico de um usuário existente pelo ID",
            tags = {"Usuários"},
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Usuário atualizado com sucesso",
                            content = @Content(schema = @Schema(implementation = UsuarioResponseDTO.class))
                    ),
                    @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content),
                    @ApiResponse(responseCode = "404", description = "Usuário não encontrado", content = @Content),
                    @ApiResponse(responseCode = "401", description = "Não autorizado (Token ausente, inválido ou expirado)", content = @Content),
                    @ApiResponse(responseCode = "403", description = "Acesso negado (Token válido, mas sem permissão)", content = @Content),
                    @ApiResponse(responseCode = "500", description = "Erro interno", content = @Content),
            },
            security = @SecurityRequirement(name = SecurityConfig.SECURITY)
    )
    ResponseEntity<UsuarioResponseDTO> updatePatch(@Parameter(description = "Id do usuário", example = "1") @PathVariable("id") Long id,
                                                   @io.swagger.v3.oas.annotations.parameters.RequestBody(
                                                           content = @Content(
                                                                   examples = {
                                                                           @ExampleObject(
                                                                                   value = "{ \"nome\": \"Maria\" }"
                                                                           )
                                                                   }
                                                           )
                                                   ) @RequestBody Map<String, Object> campos);

    @Operation(
            summary = "Remove usuário - USER E ADMIN",
            description = "Remove um usuário existente pelo ID",
            tags = {"Usuários"},
            responses = {
                    @ApiResponse(responseCode = "204", description = "Usuário removido com sucesso", content = @Content),
                    @ApiResponse(responseCode = "400", description = "Requisição inválida", content = @Content),
                    @ApiResponse(responseCode = "404", description = "Usuário não encontrado", content = @Content),
                    @ApiResponse(responseCode = "401", description = "Não autorizado (Token ausente, inválido ou expirado)", content = @Content),
                    @ApiResponse(responseCode = "403", description = "Acesso negado (Token válido, mas sem permissão)", content = @Content),
                    @ApiResponse(responseCode = "500", description = "Erro interno", content = @Content)
            },
            security = @SecurityRequirement(name = SecurityConfig.SECURITY)
    )
    ResponseEntity<?> delete(@Parameter(description = "Id do usuário", example = "1") @PathVariable("id") Long id);
}
