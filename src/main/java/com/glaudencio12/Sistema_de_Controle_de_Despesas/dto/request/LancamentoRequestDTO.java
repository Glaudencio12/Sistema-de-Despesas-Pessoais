package com.glaudencio12.Sistema_de_Controle_de_Despesas.dto.request;
import com.glaudencio12.Sistema_de_Controle_de_Despesas.models.enums.TipoLancamentoCategoria;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class LancamentoRequestDTO {
    private Long id;
    @NotBlank(message = "A descrição é obrigatória")
    @Size(min = 3, max = 150, message = "A descrição deve ter entre 3 e 150 caracteres")
    private String descricao;

    @NotNull(message = "O valor é obrigatório")
    @DecimalMin(value = "0.01", message = "O valor deve ser maior que zero")
    private BigDecimal valor;

    @NotNull(message = "A data é obrigatória")
    @PastOrPresent(message = "A data não pode estar no futuro")
    private LocalDate data;

    @NotNull(message = "O tipo de lançamento é obrigatório")
    private TipoLancamentoCategoria tipo;

    @NotNull(message = "A categoria é obrigatória")
    private Long categoriaId;

    @NotNull(message = "O usuário é obrigatório")
    private Long usuarioId;
}
