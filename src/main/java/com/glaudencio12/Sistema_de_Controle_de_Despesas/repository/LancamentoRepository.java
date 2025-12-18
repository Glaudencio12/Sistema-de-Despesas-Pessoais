package com.glaudencio12.Sistema_de_Controle_de_Despesas.repository;

import com.glaudencio12.Sistema_de_Controle_de_Despesas.models.Lancamento;
import com.glaudencio12.Sistema_de_Controle_de_Despesas.models.enums.TipoLancamentoCategoria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;


public interface LancamentoRepository extends JpaRepository<Lancamento, Long> {
    Page<Lancamento> findByUsuarioId(Long usuarioId, Pageable pageable);

    @Query("SELECT SUM(l.valor) FROM Lancamento l WHERE l.id = :id AND l.tipo = :tipo")
    BigDecimal sumValorByUsuarioIdAndTipo(@Param("id") Long id, @Param("tipo")TipoLancamentoCategoria tipo);
}
