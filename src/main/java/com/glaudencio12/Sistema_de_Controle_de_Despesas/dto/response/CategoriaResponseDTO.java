package com.glaudencio12.Sistema_de_Controle_de_Despesas.dto.response;

import com.glaudencio12.Sistema_de_Controle_de_Despesas.models.enums.TipoLancamentoCategoria;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

@Getter
@Setter
@Relation(collectionRelation = "Categoria")
public class CategoriaResponseDTO extends RepresentationModel<CategoriaResponseDTO> {
    private Long id;
    private String nome;
    private TipoLancamentoCategoria tipo;
}
