package com.glaudencio12.Sistema_de_Controle_de_Despesas.dto.response;

import com.glaudencio12.Sistema_de_Controle_de_Despesas.models.Categoria;
import com.glaudencio12.Sistema_de_Controle_de_Despesas.models.Usuario;
import com.glaudencio12.Sistema_de_Controle_de_Despesas.models.enums.TipoLancamentoCategoria;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class LancamentoResponseDTO {
    private Long id;
    private String descricao;
    private BigDecimal valor;
    private LocalDate data;
    private TipoLancamentoCategoria tipo;
    private Categoria categoria;
    private Usuario usuario;
}
