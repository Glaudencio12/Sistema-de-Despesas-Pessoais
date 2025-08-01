package com.glaudencio12.Sistema_de_Controle_de_Despesas.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.glaudencio12.Sistema_de_Controle_de_Despesas.models.Lancamento;
import com.glaudencio12.Sistema_de_Controle_de_Despesas.models.enums.TipoLancamentoCategoria;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class LancamentoResponseDTO {
    private Long id;
    private String descricao;
    private BigDecimal valor;
    private String data;
    private TipoLancamentoCategoria tipo;
    private CategoriaResponseDTO categoria;
    private UsuarioResponseDTO usuario;
}