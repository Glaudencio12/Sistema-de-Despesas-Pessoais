package com.glaudencio12.Sistema_de_Controle_de_Despesas.controllers.docs;

import com.glaudencio12.Sistema_de_Controle_de_Despesas.dto.request.LancamentoRequestDTO;
import com.glaudencio12.Sistema_de_Controle_de_Despesas.dto.response.LancamentoResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface LancamentoControllerDocs {

    @Operation(summary = "Registra um lançamento", description = "Registra uma operação de despesa ou receita", tags = { "Lançamentos" },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lançamento registrado com sucesso",
                            content = @Content(schema = @Schema(implementation = LancamentoResponseDTO.class))),
                    @ApiResponse(responseCode = "400", description = "Requisição inválida", content = @Content),
                    @ApiResponse(responseCode = "404", description = "Categoria não encontrado", content = @Content),
                    @ApiResponse(responseCode = "401", description = "Não autorizado", content = @Content),
                    @ApiResponse(responseCode = "500", description = "Erro interno no servidor", content = @Content)
            }
    )
    LancamentoResponseDTO create(@RequestBody LancamentoRequestDTO lancamento);

    @Operation(summary = "Busca um lançamento", description = "Busca uma operação pelo ID", tags = { "Lançamentos" },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lançamento encontrado com sucesso",
                            content = @Content(schema = @Schema(implementation = LancamentoResponseDTO.class))),
                    @ApiResponse(responseCode = "400", description = "Requisição inválida", content = @Content),
                    @ApiResponse(responseCode = "404", description = "Lançamento não encontrado", content = @Content),
                    @ApiResponse(responseCode = "401", description = "Não autorizado", content = @Content),
                    @ApiResponse(responseCode = "500", description = "Erro interno no servidor", content = @Content)
            }
    )
    LancamentoResponseDTO findById(@Parameter(description = "ID do lançamento", example = "1") @PathVariable("id") Long id);

    @Operation(summary = "Busca todos os lançamentos", description = "Busca todos os lançamentos realizados", tags = { "Lançamentos" },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lançamentos encontrados com sucesso",
                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = LancamentoResponseDTO.class)))),
                    @ApiResponse(responseCode = "400", description = "Requisição inválida", content = @Content),
                    @ApiResponse(responseCode = "404", description = "Lançamentos não encontrados", content = @Content),
                    @ApiResponse(responseCode = "401", description = "Não autorizado", content = @Content),
                    @ApiResponse(responseCode = "500", description = "Erro interno no servidor", content = @Content)
            }
    )
    List<LancamentoResponseDTO> findAll();
}
