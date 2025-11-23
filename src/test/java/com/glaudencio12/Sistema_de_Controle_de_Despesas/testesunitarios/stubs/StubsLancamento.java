package com.glaudencio12.Sistema_de_Controle_de_Despesas.testesunitarios.stubs;

import com.glaudencio12.Sistema_de_Controle_de_Despesas.dto.request.LancamentoRequestDTO;
import com.glaudencio12.Sistema_de_Controle_de_Despesas.dto.response.LancamentoResponseDTO;
import com.glaudencio12.Sistema_de_Controle_de_Despesas.models.Lancamento;
import com.glaudencio12.Sistema_de_Controle_de_Despesas.models.enums.TipoLancamentoCategoria;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


public class StubsLancamento {

    public static Lancamento lancamentoEntidade() {
        Lancamento lancamento = new Lancamento();
        lancamento.setId(1L);
        lancamento.setDescricao("Compra de supermercado");
        lancamento.setValor(new BigDecimal("150.75"));
        lancamento.setData(LocalDateTime.now());
        lancamento.setTipo(TipoLancamentoCategoria.DESPESA);
        lancamento.setCategoria(StubsCategoria.categoriaEntidade());
        lancamento.setUsuario(StubsUsuario.usuarioEntidade());

        return lancamento;
    }

    public static LancamentoRequestDTO lancamentoRequestDTO() {
        LancamentoRequestDTO requestDTO = new LancamentoRequestDTO();
        requestDTO.setId(1L);
        requestDTO.setDescricao("Compra de supermercado");
        requestDTO.setValor(new BigDecimal("150.75"));
        requestDTO.setTipo(TipoLancamentoCategoria.DESPESA);
        requestDTO.setCategoria(StubsCategoria.categoriaEntidade().getNome());
        requestDTO.setUsuarioId(StubsUsuario.usuarioEntidade().getId());

        return requestDTO;
    }

    public static LancamentoResponseDTO lancamentoResponseDTO() {
        LancamentoResponseDTO responseDTO = new LancamentoResponseDTO();
        responseDTO.setId(1L);
        responseDTO.setDescricao("Compra de supermercado");
        responseDTO.setValor(new BigDecimal("150.75"));
        responseDTO.setTipo(TipoLancamentoCategoria.DESPESA);
        responseDTO.setCategoria(StubsCategoria.categoriaEntidade().getNome());

        return responseDTO;
    }

    public static List<Lancamento> lancamentoEntidadeList(){
        Lancamento lancamento = new Lancamento();
        List<Lancamento> lancamentos = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            lancamento.setId(1L);
            lancamento.setDescricao("Compra de supermercado");
            lancamento.setValor(new BigDecimal("150.75"));
            lancamento.setData(LocalDateTime.now());
            lancamento.setTipo(TipoLancamentoCategoria.DESPESA);
            lancamento.setCategoria(StubsCategoria.categoriaEntidade());
            lancamento.setUsuario(StubsUsuario.usuarioEntidade());

            lancamentos.add(lancamento);
            lancamento = new Lancamento();
        }

        return lancamentos;
    }
}