package com.glaudencio12.Sistema_de_Controle_de_Despesas.dto.response;

import com.glaudencio12.Sistema_de_Controle_de_Despesas.models.enums.TipoLancamentoCategoria;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.Links;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

@Getter
@Setter
@Relation(collectionRelation = "Categoria")
@Schema(description = "Objeto de resposta da categoria")
public class CategoriaResponseDTO extends RepresentationModel<CategoriaResponseDTO> {
    @Schema(example = "1")
    private Long id;

    @Schema(example = "Casa")
    private String nome;

    @Schema(example = "DESPESA")
    private TipoLancamentoCategoria tipo;

    // Sobrescreve apenas para ocultar no Swagger
    @Override
    @Schema(hidden = true)
    public Links getLinks() {
        return super.getLinks();
    }
}
