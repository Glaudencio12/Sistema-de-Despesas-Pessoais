package com.glaudencio12.Sistema_de_Controle_de_Despesas.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

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
public class UsuarioResponseDTO extends RepresentationModel<UsuarioResponseDTO> {
    private Long id;
    private String nome;
    private String email;
    @JsonProperty("data_cadastro")
    private LocalDate dataCadastro;
}
