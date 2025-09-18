package com.glaudencio12.Sistema_de_Controle_de_Despesas.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.glaudencio12.Sistema_de_Controle_de_Despesas.models.enums.TipoLancamentoCategoria;
import lombok.Getter;
import lombok.Setter;
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
public class LancamentoResponseDTO extends RepresentationModel<LancamentoResponseDTO> {
    private Long id;
    private String descricao;
    private BigDecimal valor;
    private String data;
    private TipoLancamentoCategoria tipo;
    private String categoria;
    @JsonProperty("lancado_por")
    private String nomeUsuario;
    @JsonProperty("email_do_responsavel")
    private String emailUsuario;
}