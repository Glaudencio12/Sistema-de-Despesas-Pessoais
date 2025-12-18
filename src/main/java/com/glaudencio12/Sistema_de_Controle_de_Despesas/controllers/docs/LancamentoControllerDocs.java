package com.glaudencio12.Sistema_de_Controle_de_Despesas.controllers.docs;

import com.glaudencio12.Sistema_de_Controle_de_Despesas.config.SecurityConfig;
import com.glaudencio12.Sistema_de_Controle_de_Despesas.dto.request.LancamentoRequestDTO;
import com.glaudencio12.Sistema_de_Controle_de_Despesas.dto.response.LancamentoResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.math.BigDecimal;


public interface LancamentoControllerDocs {

    @Operation(summary = "Registra um lançamento - USER e ADMIN", description = "Registra uma operação de despesa ou receita", tags = { "Lançamentos" },
            responses = {
                    @ApiResponse(responseCode = "201", description = "Lançamento registrado com sucesso",
                            content = @Content(schema = @Schema(implementation = LancamentoResponseDTO.class))),
                    @ApiResponse(responseCode = "400", description = "Requisição inválida", content = @Content),
                    @ApiResponse(responseCode = "404", description = "Categoria não encontrado", content = @Content),
                    @ApiResponse(responseCode = "401", description = "Não autorizado (Token ausente, inválido ou expirado)", content = @Content),
                    @ApiResponse(responseCode = "403", description = "Acesso negado (Token válido, mas sem permissão)", content = @Content),
                    @ApiResponse(responseCode = "500", description = "Erro interno no servidor", content = @Content)
            },
            security = @SecurityRequirement(name = SecurityConfig.SECURITY)
    )
    ResponseEntity<LancamentoResponseDTO> create(@RequestBody LancamentoRequestDTO lancamento);

    @Operation(summary = "Busca um lançamento - USER e ADMIN", description = "Busca uma operação pelo ID", tags = { "Lançamentos" },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lançamento encontrado com sucesso",
                            content = @Content(schema = @Schema(implementation = LancamentoResponseDTO.class))),
                    @ApiResponse(responseCode = "400", description = "Requisição inválida", content = @Content),
                    @ApiResponse(responseCode = "404", description = "Lançamento não encontrado", content = @Content),
                    @ApiResponse(responseCode = "401", description = "Não autorizado (Token ausente, inválido ou expirado)", content = @Content),
                    @ApiResponse(responseCode = "403", description = "Acesso negado (Token válido, mas sem permissão)", content = @Content),
                    @ApiResponse(responseCode = "500", description = "Erro interno no servidor", content = @Content)
            },
            security = @SecurityRequirement(name = SecurityConfig.SECURITY)
    )
    ResponseEntity<LancamentoResponseDTO> findById(@Parameter(description = "ID do lançamento", example = "1") @PathVariable("id") Long id);

    @Operation(summary = "Busca todos os lançamentos - USER e ADMIN", description = "Busca todos os lançamentos realizados", tags = { "Lançamentos" },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lançamentos encontrados com sucesso",
                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = LancamentoResponseDTO.class)))),
                    @ApiResponse(responseCode = "400", description = "Requisição inválida", content = @Content),
                    @ApiResponse(responseCode = "404", description = "Lançamentos não encontrados", content = @Content),
                    @ApiResponse(responseCode = "401", description = "Não autorizado (Token ausente, inválido ou expirado)", content = @Content),
                    @ApiResponse(responseCode = "403", description = "Acesso negado (Token válido, mas sem permissão)", content = @Content),
                    @ApiResponse(responseCode = "500", description = "Erro interno no servidor", content = @Content)
            },
            security = @SecurityRequirement(name = SecurityConfig.SECURITY)
    )
    ResponseEntity<PagedModel<EntityModel<LancamentoResponseDTO>>> findAll(
            @PageableDefault(
                    page = 0,
                    size = 12,
                    direction = Sort.Direction.ASC,
                    sort = "data"
            )Pageable pageable
    );

    @Operation(summary = "Calcula o total de despesas - USER e ADMIN", description = "Soma todos os lançamentos do tipo DESPESA vinculados ao usuário autenticado", tags = { "Lançamentos" },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Cálculo realizado com sucesso",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = BigDecimal.class, example = "1250.50"))
                    ),
                    @ApiResponse(responseCode = "401", description = "Não autorizado (Token ausente, inválido ou expirado)", content = @Content),
                    @ApiResponse(responseCode = "403", description = "Acesso negado (Token válido, mas sem permissão)", content = @Content),
                    @ApiResponse(responseCode = "500", description = "Erro interno no servidor", content = @Content)
            },
            security = @SecurityRequirement(name = SecurityConfig.SECURITY)
    )
    public BigDecimal balanceExpenses();
}
