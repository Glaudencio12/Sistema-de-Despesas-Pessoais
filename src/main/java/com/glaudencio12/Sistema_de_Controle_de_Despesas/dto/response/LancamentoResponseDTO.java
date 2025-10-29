package com.glaudencio12.Sistema_de_Controle_de_Despesas.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.glaudencio12.Sistema_de_Controle_de_Despesas.models.enums.TipoLancamentoCategoria;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.Links;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.math.BigDecimal;

@Getter
@Setter
@JsonPropertyOrder({
        "id",
        "valor",
        "data",
        "tipo",
        "categoria",
        "descricao",
        "lancado_por",
        "email_do_responsavel",
        "links"
})
@Relation(collectionRelation = "Lancamento")
@Schema(description = "Objeto de resposta do lançamento")
public class LancamentoResponseDTO extends RepresentationModel<LancamentoResponseDTO> {
    @Schema(example = "1")
    private Long id;

    @Schema(example = "Compra de material escolar")
    private String descricao;

    @Schema(example = "125.50")
    private BigDecimal valor;

    @Schema(example = "2025-10-29")
    private String data;

    @Schema(example = "DESPESA")
    private TipoLancamentoCategoria tipo;

    @Schema(example = "Educação")
    private String categoria;

    @JsonProperty("lancado_por")
    @Schema(example = "Maria Oliveira")
    private String nomeUsuario;

    @JsonProperty("email_do_responsavel")
    @Schema(example = "maria.oliveira@gmail.com")
    private String emailUsuario;

    // Sobrescreve apenas para ocultar no Swagger
    @Override
    @Schema(hidden = true)
    public Links getLinks() {
        return super.getLinks();
    }

}