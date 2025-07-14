package com.glaudencio12.Sistema_de_Controle_de_Despesas.dto.request;
import com.glaudencio12.Sistema_de_Controle_de_Despesas.models.enums.TipoLancamentoCategoria;
import lombok.Data;

@Data
public class CategoriaRequestDTO {
    private Long id;
    private String nome;
    private TipoLancamentoCategoria tipo;
    private Long usuarioId;
}
