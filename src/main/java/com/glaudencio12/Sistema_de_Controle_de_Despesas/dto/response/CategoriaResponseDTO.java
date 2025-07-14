package com.glaudencio12.Sistema_de_Controle_de_Despesas.dto.response;

import com.glaudencio12.Sistema_de_Controle_de_Despesas.models.Usuario;
import com.glaudencio12.Sistema_de_Controle_de_Despesas.models.enums.TipoLancamentoCategoria;
import lombok.Data;

@Data
public class CategoriaResponseDTO {
    private Long id;
    private String nome;
    private TipoLancamentoCategoria tipo;
    private Usuario usuario;
}
