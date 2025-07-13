package com.glaudencio12.Sistema_de_Controle_de_Despesas.models;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "usuario")
@Data
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 100)
    private String nome;
    @Column(nullable = false, length = 60, unique = true)
    private String email;
    @Column(nullable = false, length = 100)
    private String senha;
    @Column(name = "data_cadastro", nullable = false)
    private LocalDate dataCadastro;
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
    private List<Categoria> categorias = new ArrayList<>();
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
    private List<Lancamento> lancamentos = new ArrayList<>();

}
