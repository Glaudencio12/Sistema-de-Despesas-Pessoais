package com.glaudencio12.Sistema_de_Controle_de_Despesas.dto.request;
import com.glaudencio12.Sistema_de_Controle_de_Despesas.models.enums.TipoLancamentoCategoria;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class LancamentoRequestDTO {
    private Long id;
    private String descricao;
    private BigDecimal valor;
    private LocalDate data;
    private TipoLancamentoCategoria tipo;
    private Long categoriaId;
    private Long usuarioId;
}
