package com.glaudencio12.Sistema_de_Controle_de_Despesas.controllers.docs;

import com.glaudencio12.Sistema_de_Controle_de_Despesas.dto.request.CategoriaRequestDTO;
import com.glaudencio12.Sistema_de_Controle_de_Despesas.dto.response.CategoriaResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

public interface CategoriaControllerDocs {

    @Operation(summary = "Cria uma categoria", description = "Cria uma nova categoria associada ao usuário.", tags = { "Categorias" },
            responses = {
                    @ApiResponse(responseCode = "201", description = "Categoria registrada com sucesso",
                            content = @Content(schema = @Schema(implementation = CategoriaResponseDTO.class))),
                    @ApiResponse(responseCode = "400", description = "Requisição inválida", content = @Content),
                    @ApiResponse(responseCode = "401", description = "Não autorizado (Token ausente, inválido ou expirado)", content = @Content),
                    @ApiResponse(responseCode = "403", description = "Acesso negado (Token válido, mas sem permissão)", content = @Content),
                    @ApiResponse(responseCode = "409", description = "Categoria duplicada", content = @Content),
                    @ApiResponse(responseCode = "500", description = "Erro interno no servidor", content = @Content)
            }
    )
    ResponseEntity<CategoriaResponseDTO> create(@RequestBody CategoriaRequestDTO categoriaRequest);

    @Operation(summary = "Busca uma categoria", description = "Recupera os detalhes de uma categoria a partir do seu ID.", tags = { "Categorias" },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Categoria encontrada com sucesso",
                            content = @Content(schema = @Schema(implementation = CategoriaResponseDTO.class))),
                    @ApiResponse(responseCode = "400", description = "Requisição inválida", content = @Content),
                    @ApiResponse(responseCode = "401", description = "Não autorizado (Token ausente, inválido ou expirado)", content = @Content),
                    @ApiResponse(responseCode = "403", description = "Acesso negado (Token válido, mas sem permissão)", content = @Content),
                    @ApiResponse(responseCode = "404", description = "Categoria não encontrada", content = @Content),
                    @ApiResponse(responseCode = "500", description = "Erro interno no servidor", content = @Content)
            }
    )
    ResponseEntity<CategoriaResponseDTO> findById(@Parameter(description = "ID da categoria", example = "1") @PathVariable("id") Long id);

    @Operation(summary = "Lista todas as categorias", description = "Lista todas as categorias cadastradas pelo usuário.", tags = { "Categorias" },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Categorias encontradas com sucesso",
                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = CategoriaResponseDTO.class)))),
                    @ApiResponse(responseCode = "400", description = "Requisição inválida", content = @Content),
                    @ApiResponse(responseCode = "401", description = "Não autorizado (Token ausente, inválido ou expirado)", content = @Content),
                    @ApiResponse(responseCode = "403", description = "Acesso negado (Token válido, mas sem permissão)", content = @Content),
                    @ApiResponse(responseCode = "404", description = "Nenhuma categoria encontrada", content = @Content),
                    @ApiResponse(responseCode = "500", description = "Erro interno no servidor", content = @Content)
            }
    )
    ResponseEntity<PagedModel<EntityModel<CategoriaResponseDTO>>> findAll(
            @PageableDefault(
                    page = 0,
                    size = 12,
                    direction = Sort.Direction.ASC,
                    sort = "nome"
            )Pageable pageable
    );

    @Operation(summary = "Exclui uma categoria", description = "Remove uma categoria a partir do seu ID.", tags = { "Categorias" },
            responses = {
                    @ApiResponse(responseCode = "204", description = "Categoria excluída com sucesso", content = @Content),
                    @ApiResponse(responseCode = "400", description = "Requisição inválida", content = @Content),
                    @ApiResponse(responseCode = "401", description = "Não autorizado (Token ausente, inválido ou expirado)", content = @Content),
                    @ApiResponse(responseCode = "403", description = "Acesso negado (Token válido, mas sem permissão)", content = @Content),
                    @ApiResponse(responseCode = "404", description = "Categoria não encontrada", content = @Content),
                    @ApiResponse(responseCode = "500", description = "Erro interno no servidor", content = @Content),
            }
    )
    ResponseEntity<Void> delete(String nomeCategoria);
}
