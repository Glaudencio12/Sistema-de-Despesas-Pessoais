package com.glaudencio12.Sistema_de_Controle_de_Despesas.mapper;

import com.glaudencio12.Sistema_de_Controle_de_Despesas.dto.response.LancamentoResponseDTO;
import com.glaudencio12.Sistema_de_Controle_de_Despesas.models.Lancamento;

public class LancamentoMapper {
    public static LancamentoResponseDTO toResponseDTO(Lancamento lancamento) {
        LancamentoResponseDTO dto = new LancamentoResponseDTO();
        dto.setId(lancamento.getId());
        dto.setDescricao(lancamento.getDescricao());
        dto.setValor(lancamento.getValor());
        dto.setData(lancamento.getData());
        dto.setTipo(lancamento.getTipo());

        dto.setCategoria(lancamento.getCategoria().getNome());
        dto.setNomeUsuario(lancamento.getUsuario().getNome());
        dto.setEmailUsuario(lancamento.getUsuario().getEmail());

        return dto;
    }
}
