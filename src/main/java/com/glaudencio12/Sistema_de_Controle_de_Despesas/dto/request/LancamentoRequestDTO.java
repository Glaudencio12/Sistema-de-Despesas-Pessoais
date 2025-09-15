package com.glaudencio12.Sistema_de_Controle_de_Despesas.dto.request;
import com.glaudencio12.Sistema_de_Controle_de_Despesas.models.enums.TipoLancamentoCategoria;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class LancamentoRequestDTO {
    private Long id;

    @NotBlank(message = "A descrição é obrigatória")
    @Size(min = 3, max = 150, message = "A descrição deve ter entre 3 e 150 caracteres")
    private String descricao;

    @NotNull(message = "O valor é obrigatório")
    @DecimalMin(value = "0.01", message = "O valor deve ser maior que zero")
    private BigDecimal valor;

    @NotNull(message = "O tipo de lançamento é obrigatório")
    private TipoLancamentoCategoria tipo;

    @NotBlank(message = "A categoria é obrigatória")
    @Size(min = 4, max = 50, message = "A categoria deve ter entre 3 e 150 caracteres")
    private String categoria;

    @NotNull(message = "O usuário é obrigatório")
    private Long usuarioId;
}
