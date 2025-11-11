package com.glaudencio12.Sistema_de_Controle_de_Despesas.repository;

import com.glaudencio12.Sistema_de_Controle_de_Despesas.models.Lancamento;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;

public interface LancamentoRepository extends JpaRepository<Lancamento, Long> {
    Page<Lancamento> findByUsuarioId(Long usuarioId, Pageable pageable);
}
