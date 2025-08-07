package com.glaudencio12.Sistema_de_Controle_de_Despesas.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UsuarioRequestDTO {
    private Long id;

    @NotBlank(message = "O nome do usuário é obrigatório")
    @Size(min = 3, max = 100, message = "O nome do usuário precisa ter entre 3 a 100 caracteres")
    private String nome;

    @NotBlank(message = "O email do usuário é obrigatório")
    @Email(message = "O emial precisa ser válido")
    private String email;

    @NotBlank(message = "A senha é obrigatória")
    @Size(min = 6, max = 10, message = " A senha precisa ter entre 6 e 10 caracteres")
    private String senha;

    @JsonProperty("data_cadastro")
    @Schema(hidden = true)
    private LocalDate dataCadastro;
}
