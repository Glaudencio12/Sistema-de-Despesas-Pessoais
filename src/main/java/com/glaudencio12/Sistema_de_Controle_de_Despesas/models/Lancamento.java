package com.glaudencio12.Sistema_de_Controle_de_Despesas.models;

import com.glaudencio12.Sistema_de_Controle_de_Despesas.models.enums.TipoLancamentoCategoria;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "lancamento")
@Data
public class Lancamento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 150)
    private String descricao;
    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal valor;
    @Column(nullable = false)
    private LocalDateTime data;
    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private TipoLancamentoCategoria tipo;
    @ManyToOne
    @JoinColumn(name = "categoria_id", referencedColumnName = "id")
    private Categoria categoria;
    @ManyToOne
    @JoinColumn(name = "usuario_id", referencedColumnName = "id")
    private Usuario usuario;
}
