package com.glaudencio12.Sistema_de_Controle_de_Despesas.models;

import com.glaudencio12.Sistema_de_Controle_de_Despesas.models.enums.TipoLancamentoCategoria;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "categoria")
@Getter
@Setter
public class Categoria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String nome;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private TipoLancamentoCategoria tipo;

    @ManyToOne
    @JoinColumn(name = "usuario_id", referencedColumnName = "id")
    private Usuario usuario;

    @OneToMany(mappedBy = "categoria", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Lancamento> lancamentos = new ArrayList<>();
}
