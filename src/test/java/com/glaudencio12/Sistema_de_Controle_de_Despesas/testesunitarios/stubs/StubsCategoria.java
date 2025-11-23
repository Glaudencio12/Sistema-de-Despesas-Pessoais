package com.glaudencio12.Sistema_de_Controle_de_Despesas.testesunitarios.stubs;

import com.glaudencio12.Sistema_de_Controle_de_Despesas.dto.request.CategoriaRequestDTO;
import com.glaudencio12.Sistema_de_Controle_de_Despesas.dto.response.CategoriaResponseDTO;
import com.glaudencio12.Sistema_de_Controle_de_Despesas.models.Categoria;
import com.glaudencio12.Sistema_de_Controle_de_Despesas.models.enums.TipoLancamentoCategoria;

import java.util.ArrayList;
import java.util.List;

public class StubsCategoria {

    public static Categoria categoriaEntidade(){
        Categoria categoria = new Categoria();

        categoria.setId(1L);
        categoria.setNome("Categoria teste");
        categoria.setTipo(TipoLancamentoCategoria.DESPESA);
        categoria.setUsuario(StubsUsuario.usuarioEntidade());
        return categoria;
    }

    public static List<Categoria> categoriaEntidadeList(){
        Categoria categoria = new Categoria();
        List<Categoria> categorias = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            categoria.setId(1L);
            categoria.setNome("Categoria teste");
            categoria.setTipo(TipoLancamentoCategoria.DESPESA);
            categoria.setUsuario(StubsUsuario.usuarioEntidade());

            categorias.add(categoria);
            categoria = new Categoria();
        }

        return categorias;
    }

    public static CategoriaRequestDTO categoriaRequest(){
        CategoriaRequestDTO requestDTO = new CategoriaRequestDTO();

        requestDTO.setId(1L);
        requestDTO.setNome("Categoria teste");
        requestDTO.setTipo(TipoLancamentoCategoria.DESPESA);
        requestDTO.setUsuarioId(StubsUsuario.usuarioEntidade().getId());
        return requestDTO;
    }

    public static CategoriaResponseDTO categoriaResponse(){
        CategoriaResponseDTO requestDTO = new CategoriaResponseDTO();

        requestDTO.setId(1L);
        requestDTO.setNome("Categoria teste");
        requestDTO.setTipo(TipoLancamentoCategoria.DESPESA);
        return requestDTO;
    }

}
