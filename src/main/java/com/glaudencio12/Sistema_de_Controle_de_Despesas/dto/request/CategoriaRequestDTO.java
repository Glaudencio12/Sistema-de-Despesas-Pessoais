package com.glaudencio12.Sistema_de_Controle_de_Despesas.dto.request;
import com.glaudencio12.Sistema_de_Controle_de_Despesas.models.enums.TipoLancamentoCategoria;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CategoriaRequestDTO {
    private Long id;

    @NotBlank(message = "O nome da categoria é obrigatório")
    @Size(min = 3, max = 50, message = "O nome precisa ter entre 3 e 50 caracteres")
    private String nome;

    @NotNull(message = "O tipo da categoria é obrigatório")
    private TipoLancamentoCategoria tipo;

    @NotNull(message = "O usuário é obrigatório")
    private Long usuarioId;
}
