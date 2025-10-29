package com.glaudencio12.Sistema_de_Controle_de_Despesas.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.Links;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.time.LocalDate;

@Getter
@Setter
@JsonPropertyOrder({
        "id",
        "nome",
        "email",
        "data_cadastro",
        "links"
})
@Relation(collectionRelation = "Usuario")
@Schema(description = "Objeto de resposta do usuário")
public class UsuarioResponseDTO extends RepresentationModel<UsuarioResponseDTO> {
    @Schema(example = "1")
    private Long id;

    @Schema(example = "Maria")
    private String nome;

    @Schema(example = "maria@gmail.com")
    private String email;

    @JsonProperty("data_cadastro")
    @Schema(example = "2025-10-29")
    private LocalDate dataCadastro;

    // Sobrescreve apenas para ocultar no Swagger
    @Override
    @Schema(hidden = true)
    public Links getLinks() {
        return super.getLinks();
    }
}
